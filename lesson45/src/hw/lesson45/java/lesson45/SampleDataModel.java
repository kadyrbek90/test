package hw.lesson45.java.lesson45;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleDataModel {
    private User user;
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private List<User> customers = new ArrayList<>();

    public SampleDataModel() {
        customers.add(new User("Amos", "Burton", "'Timmy'"));
        customers.get(0).setEmailConfirmed(true);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public List<User> getCustomers() {
        return customers;
    }

    public void setCustomers(List<User> customers) {
        this.customers = customers;
    }

    public void addCustomer(User user) { this.customers.add(user); }

    public static class User {
        private boolean emailConfirmed = false;
        private String email;
        private String login;
        private String password;

        public User(String email, String login, String password) {
            this.email = email;
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isEmailConfirmed() {
            return emailConfirmed;
        }

        public void setEmailConfirmed(boolean emailConfirmed) {
            this.emailConfirmed = emailConfirmed;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
