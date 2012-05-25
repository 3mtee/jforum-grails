import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class JforumServiceTests extends GroovyTestCase {
    private static final String API_KEY = CH.config.grails.jforum.apikey
    private static final String USERNAME = 'emtee'
    private static final String EMAIL = 'emtee@gmail.com'
    private static final String PASSWORD = 'test'

    def jforumService

    private String getUserEmail() {
        def email = jforumService.getJforumUserEmail()
        if (email.isEmpty()) {
            def created = jforumService.createJForumUser(API_KEY, USERNAME, EMAIL, PASSWORD)
            if (created) {
                email = EMAIL
            } else {
                throw new Exception('User has not been created')
            }
        }
        email
    }

    void testUserRetrieval() {
        def xml = jforumService.geJForumUserEmailXML()
        assert xml.@"response-code" == 'OK'
        def userCount = xml.users.@total
        assert userCount != null
    }

    void testInboxUnreadMessages() {
        def email = getUserEmail()
        def xml = jforumService.getInboxUnreadMessagesXML(API_KEY, email)
        assert xml.@"response-code" == 'OK'
        assert xml.messages.@total != null

    }

    void testInboxAllMessages() {
        def email = getUserEmail()
        def xml = jforumService.getInboxAllMessagesXML(API_KEY, email)
        assert xml.@"response-code" == 'OK'
        assert xml.messages.@total != null
    }

    void testSendPM() {
        def email = getUserEmail()
        def count = jforumService.getInboxTotalMessagesCount(API_KEY, email)
        def start = System.currentTimeMillis()
        jforumService.sendPrivateMessage(API_KEY, 'Emtee', email, 'test', 'hi there')
        def finish = System.currentTimeMillis()
        def duration = finish - start
        log.info("request duration (ms): " + duration)
        def newCount = jforumService.getInboxTotalMessagesCount(API_KEY, email)
        assertEquals count + 1, newCount
    }
}
