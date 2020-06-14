package com.ggomak.springboot2.controller;

import com.ggomak.springboot2.oauthsecurity.annotation.LoginUser;
import com.ggomak.springboot2.oauthsecurity.auth.dto.SessionUser;
import com.ggomak.springboot2.service.ContentService;
import com.ggomak.springboot2.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @reference : http://aodis.egloos.com/5962812 * @modified : whiteduck
 */

@RestController
@RequiredArgsConstructor
public class ContentApiController {

    private final ContentService contentService;
    private final MainService mainService;

    @GetMapping("/api/v1/content/{content_id}")
    public void getContentMediaVideo(@PathVariable String content_id,
                                     HttpServletRequest request, HttpServletResponse response, @LoginUser SessionUser user) throws IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();

        String strDate = dateFormat.format(date);

        String sessionAddress = request.getRemoteAddr();

        if(user != null){
            mainService.regist(sessionAddress, user);
            System.out.println(strDate +"   LOG 0001 --- [     system log] Request Contents 000" + content_id + ": { Session Address : " + sessionAddress + " }\t{ User : " + user.getEmail() + " }");
        }

        long id = Long.parseLong(content_id);

        contentService.getContent(id, request, response);

    }
}