package jforum.grails

class JforumService {

    def getJforumUserEmail() {
        def emailAddress = ''
        def xml = geJForumUserEmailXML()
        def userCount = xml.users.@total
        for (int i = 0; i < userCount.toInteger(); i++) {
            def email = xml.users.user[i].@email
            assert email != null
            emailAddress = email.toString()
            if (!emailAddress.isEmpty()) {
                break;
            }
        }

        return emailAddress
    }

    def geJForumUserEmailXML(def apiKey) {
        def xml = null
        withHttp(uri: "http://localhost:8080") {
            xml = get(path: '/jforum/userApi/list/' + apiKey +'.page')
        }
        xml
    }

    def createJForumUser(def apiKey, def username, def email, def password) {
        withHttp(uri: "http://localhost:8080") {
            def xml = get(path: '/jforum/userApi/insert.page',
                    query: [api_key: apiKey, username: username, email: email, password: password])
            assert xml.@"response-code" == 'OK'
            assert xml.user.@id != null
            return true
        }
        return false
    }

    def getInboxUnreadMessagesCount(def apiKey, def email) {
        def xml = getInboxUnreadMessagesXML(apiKey, email)
        xml.messages.@total.toInteger()
    }

    def getInboxUnreadMessagesXML(def apiKey, def email) {
        def xml = null
        withHttp(uri: "http://localhost:8080") {
            xml = get(path: '/jforum/pMessageApi/count.page',
                    query: [api_key: apiKey, email: email, messageType: 'new'])

        }
        xml
    }

    def getInboxAllMessagesXML(def apiKey, def email) {
        def xml = null
        withHttp(uri: "http://localhost:8080") {
            xml = get(path: '/jforum/pMessageApi/count.page',
                    query: [api_key: apiKey, email: email, messageType: 'all'])

        }
        xml
    }

    def getInboxTotalMessagesCount(def apiKey, def email) {
        def xml = getInboxAllMessagesXML(apiKey, email)
        assert xml.@"response-code" == 'OK'
        assert xml.messages.@total != null
        xml.messages.@total.toInteger()
    }

    def sendPrivateMessage(def apiKey, String recepient, def senderEmail, def subject, def message) {
        withHttp(uri: "http://localhost:8080") {
            def xml = get(path: '/jforum/pMessageApi/writeMessage.page',
                    query: [api_key: apiKey, toUsername: recepient, email: senderEmail, subject: subject, message: message])
            assert xml.@"response-code" == 'OK'
        }
    }
}
