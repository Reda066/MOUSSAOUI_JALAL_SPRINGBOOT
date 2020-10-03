package com.course.client.controllers;

import com.course.client.beans.CartBean;
import com.course.client.beans.CartItemBean;
import com.course.client.beans.OrderBean;
import com.course.client.beans.ProductBean;
import com.course.client.proxies.MsCartProxy;
import com.course.client.proxies.MsOrderProxy;
import com.course.client.proxies.MsProductProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private MsProductProxy msProductProxy;

    @Autowired
    private MsCartProxy msCartProxy;

    @Autowired
    private MsOrderProxy msOrderProxy;

    @RequestMapping("/")
    public String index(Model model, HttpSession httpSession, @PathParam("order") Integer order) {
        List<ProductBean> products =  msProductProxy.list();

        CartBean cart = this.prepareCart(httpSession);
        int cartSize = cart.getProducts() == null ? 0 : cart.getProducts().size();
        System.out.println(order);
        model.addAttribute("order", order);
        model.addAttribute("products", products);
        model.addAttribute("cartSize", cartSize);

        return "index";
    }

    @RequestMapping("/product/{id}")
    public String product(Model model, @PathVariable("id") long id, HttpSession httpSession) {

        CartBean cart = this.prepareCart(httpSession);
        int cartSize = cart.getProducts() == null ? 0 : cart.getProducts().size();

        ProductBean product = msProductProxy.get(id);

        model.addAttribute("product", product);
        model.addAttribute("cartSize", cartSize);

        return "product";
    }

    @RequestMapping("/product/add/{id}")
    public String productAdd(@PathVariable("id") long id, @RequestParam("quantity") int quantity, HttpSession httpSession) {
        CartBean cart = prepareCart(httpSession);

        ProductBean product = msProductProxy.get(id);
        if(product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not available");
        }

        CartItemBean cartItemBean = new CartItemBean();
        cartItemBean.setProductId(id);
        cartItemBean.setQuantity(quantity != 0 ? quantity : 1);
        cartItemBean.setProduct(product);
        msCartProxy.addProductToCart(cart.getId(), cartItemBean);

        if(cart.getProducts() == null)
            cart.setProducts(new ArrayList<CartItemBean>());


        cart.addProduct(cartItemBean);

        httpSession.setAttribute("cart", cart);

        return "redirect:/product/" + id;
    }
    @RequestMapping("/cart")
    public String cart(Model model, HttpSession httpSession) {
        CartBean cart = this.prepareCart(httpSession);
        int cartSize = cart.getProducts() == null ? 0 : cart.getProducts().size();
        double total = 0;
        if(cartSize != 0) {
            for (CartItemBean item: cart.getProducts()) {
                total += item.getQuantity() * item.getProduct().getPrice();
            }
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartSize", cartSize);
        model.addAttribute("total", total);
        return "cart";
    }

    @RequestMapping("/order")
    public String order(Model model, HttpSession httpSession) {
        CartBean cart = (CartBean) httpSession.getAttribute("cart");

        if(cart == null || cart.getProducts() == null || cart.getProducts().size() == 0)
            return "redirect:/cart";

        OrderBean order = new OrderBean();
        order.setProducts(cart.getProducts());
        order.calculateTotal();
        if(msOrderProxy.saveOrder(order).getBody() != null){
            httpSession.setAttribute("cart", new CartBean());
            return "redirect:/?order=1";
        }
        else {
            return "redirect:/?order=0";

        }
    }

    public CartBean prepareCart(HttpSession httpSession) {
        CartBean cart = (CartBean) httpSession.getAttribute("cart");

        if(cart == null) {
            cart = msCartProxy.createNewCart(new CartBean()).getBody();
            httpSession.setAttribute("cart", cart);
        }

        return cart;
    }
}
