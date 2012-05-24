package jforum.grails

class JforumService {

    private String email = null

    def getJforumUserEmail() {
        withHttp(uri: "http://localhost:8080") {
            def xml = get(path : '/jforum/userApi/list/qwerty.page')
            assert xml.@"response-code" == 'OK'
            def userCount = xml.users.@total
            assert userCount != null
            for(int i = 0; i < userCount.toInteger(); i++) {
                def email = xml.users.user[i].@email
                assert email != null
                String emailStr = email.toString()
                if (!emailStr.isEmpty()) {
                    this.email = emailStr
                    break;
                }
            }
            if (this.email == null) {
               createJForumUser()

            }
            assert xml.messages.@total != null
        }
    }

    def createJForumUser() {
        withHttp(uri: "http://localhost:8080") {
            def xml = get(path : '/jforum/userApi/insert/qwerty/emtee/emtee@gmail.com/test.page')
            assert xml.@"response-code" == 'OK'


            assert xml.user.@id != null
        }
    }

    def getInboxUnreadMessagesCount() {
        if (email == null) {
            getJforumUserEmail()
        }
        withHttp(uri: "http://localhost:8080") {
            def xml = get(path : '/jforum/pMessageApi/count.page',
                    query: [api_key: 'qwerty', email: email, messageType: 'new'])
            assert xml.@"response-code" == 'OK'
            assert xml.messages.@total != null
        }
    }

    def getInboxTotalMessagesCount() {
        if (email == null) {
            getJforumUserEmail()
        }
        withHttp(uri: "http://localhost:8080") {
            def xml = get(path : '/jforum/pMessageApi/count.page',
                    query: [api_key: 'qwerty', email: email, messageType: 'all'])
            assert xml.@"response-code" == 'OK'
            assert xml.messages.@total != null
        }
    }
}
