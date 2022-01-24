package com.example.lecture2.websocket_stomp.repository;

import com.example.lecture2.websocket_stomp.model.ChatRoomDto;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoomDto> chatRoomMap;

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDto> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDto> result = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(result);
        return result;
    }

    public ChatRoomDto findRoomById(String id){
        return chatRoomMap.get(id);
    }

    public ChatRoomDto createChatRoomDTO(String name){
        ChatRoomDto room = ChatRoomDto.create(name);
        chatRoomMap.put(room.getRoomId(), room);
        return room;
    }
}
