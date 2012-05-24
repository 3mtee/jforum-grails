package jforum.grails

import javax.servlet.http.Cookie
import uk.co.smartkey.jforumsecuresso.SecurityTools

class JforumFilters {
    def springSecurityService
    def filters = {
        all(controller: '*', action: '*') {
            before = {
                request.remoteUser
                User user = springSecurityService.currentUser

                if (user) {
                    String encryptedData = SecurityTools.getInstance().encryptCookieValues(user.email, user.username);

                    Cookie c = new Cookie(SecurityTools.FORUM_COOKIE_NAME, encryptedData)
                    c.maxAge = -1;
                    c.path = "/"
                    response.addCookie(c)
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
