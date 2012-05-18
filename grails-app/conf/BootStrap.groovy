import jforum.grails.User

class BootStrap {

    def init = { servletContext ->
        new User(email: "emtee@gmail.com", username: "emtee", password: "secret", enabled: true).save()
    }
    def destroy = {
    }
}
