package jforum.grails

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class PostController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def jforumService
    def springSecurityService

    def index() {
        def apikey = CH.config.grails.jforum.apikey
        def user = springSecurityService.currentUser
        def messagesCount = 0
        if (user != null) {
            messagesCount = jforumService.getInboxUnreadMessagesCount(apikey, user.email)
        }
        [messages: messagesCount]
    }


}
