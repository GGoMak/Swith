package com.ggomak.springboot2.service;

import com.ggomak.springboot2.domain.Content;
import com.ggomak.springboot2.domain.Data;
import com.ggomak.springboot2.domain.User;
import com.ggomak.springboot2.oauthsecurity.auth.dto.SessionUser;
import com.ggomak.springboot2.repository.ContentRepository;
import com.ggomak.springboot2.repository.DataRepository;
import com.ggomak.springboot2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public void save(String sessionAddress, String concendata){

        Date date = new Date();
        String strDate = dateFormat.format(date);

        Optional<User> user = userRepository.findBySessionAddress(sessionAddress);

        try {
            Long contentId = user.get().getSessionContent();

            if(contentId == null){
                System.out.println(strDate + "   LOG 0004 --- [     system log]" + "No Contents Session: { User : " + user.get().getEmail() + " }");
                return;
            }

            Content content = contentRepository.findByContentNumber(contentId);
            Data data = dataRepository.findByUserAndContent(user.get(), content);

            if (data == null) {
                dataRepository.save(Data.builder()
                        .user(user.get())
                        .content(content)
                        .concentrateData(concendata+":")
                        .build());
            } else {
                data.setData(data.getConcentrateData().concat(concendata+":"));
                dataRepository.save(data);
            }

            System.out.println(strDate + "   LOG 0002 --- [     system log]" + " Save Concentrate Data: { User : " + user.get().getEmail() + " } " + "{ Content ID: " + contentId + " } { Value: " + concendata + " }");
        }
        catch(NoSuchElementException e){
            System.out.println(strDate + "   LOG 0004 --- [     system log]" + " No Contents Session: { User : " + user.get().getEmail() + " }");
        }

    }

    public String load(SessionUser sessionUser, Long contentNumber){

        Date date = new Date();
        String strDate = dateFormat.format(date);

        Optional<User> user = userRepository.findByEmailAndSocialType(sessionUser.getEmail(), sessionUser.getSocialType());
        Content content = contentRepository.findByContentNumber(contentNumber);

        Data data = dataRepository.findByUserAndContent(user.get(), content);

        if(data == null){
            return null;
        }

        System.out.println(strDate + "   LOG 0003 --- [     system log]" + " Request Concentrate Data: { User : " + user.get().getEmail() + " }");

        return data.getConcentrateData();
    }
}
