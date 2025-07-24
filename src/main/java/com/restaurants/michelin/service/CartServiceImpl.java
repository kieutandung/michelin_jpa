    package com.restaurants.michelin.service;

    import com.restaurants.michelin.model.Cart;
    import com.restaurants.michelin.model.Food;
    import com.restaurants.michelin.model.User;
    import com.restaurants.michelin.repository.CartRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;


    @Service
    public class CartServiceImpl implements CartService<Cart> {
        @Autowired
        private CartRepository cartRepository;
        @Override
        public List<Cart> findCartByUser(User user) {
            return cartRepository.findCartByUser(user);
        }
        @Override
        public void save(Cart cart) {
            cartRepository.save(cart);
        }

        @Override
        public Cart findByUserAndFood(User user, Food food) {
            return cartRepository.findByUserAndFood(user,food);
        }
        @Override
        public void removeItemFromCart(Integer idCart, User user) {
            Cart cart = cartRepository.findByIdCartAndUser(idCart, user);
            if (cart != null) {
                cartRepository.delete(cart);
            }
        }
        @Override
        public void updateQuantity(Integer idCart, int newQuantity) {
            cartRepository.updateQuantity(idCart,newQuantity);
        }

        @Override
        public Cart getCartByIdCart(Integer idCart) {
            return cartRepository.getCartByIdCart(idCart);
        }

        @Override
        public void deleteAllCartByUser(User user) {
            cartRepository.deleteByUser(user);
        }

    }
