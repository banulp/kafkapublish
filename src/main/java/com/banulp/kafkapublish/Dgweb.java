package com.banulp.kafkapublish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Dgweb {

    private int articleNo = 0;

    @Autowired
    private KafkaSendMessage ksm;

    public void pollAndSM() {

        List<String> list = new ArrayList<String>();
        list.add("{\"id\":\"206800996\",\"title\":\"노트북\",\"region\":\"성남시 분당구 판교동\"}");
        list.add("{\"id\":\"206908580\",\"title\":\"와플 팬\",\"region\":\"성남시 분당구 운중동\"}");
        list.add("{\"id\":\"206721398\",\"title\":\"레고블록\",\"region\":\"성남시 중원구 성남동\"}");
        list.add("{\"id\":\"206721398\",\"title\":\"레고블록\",\"region\":\"성남시 중원구 성남동\"}");
        list.add("{\"id\":\"206721398\",\"title\":\"레고블록\",\"region\":\"성남시 중원구 성남동\"}");
        list.add("{\"id\":\"206721398\",\"title\":\"레고블록\",\"region\":\"성남시 중원구 성남동\"}");
        list.forEach(l -> {
                    System.out.println("in");
                    ksm.sendMessage(l);
                }
        );
    }

}
