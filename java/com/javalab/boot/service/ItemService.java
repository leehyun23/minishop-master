package com.javalab.boot.service;

import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.item.ItemRepository;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.ItemDto;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 상품 등록
    public void save(ItemDto itemDto, MultipartFile file) throws Exception {
        Item item = ItemDto.dtoToEntity(itemDto); // ItemDto를 Item 엔터티로 변환

        if (file != null) {
            // 파일이 전달된 경우 파일 저장 로직 추가
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
            item.setFilename(fileName);
            item.setFilepath("/files/" + fileName);
        } else {
            // 파일이 전달되지 않은 경우의 처리
            item.setFilepath("https://dummyimage.com/450x300/dee2e6/6c757d.jpg");
        }

        item.setCount(item.getStock());
        item.setSoldout(true);

        itemRepository.save(item);
    }
    // 전체 상품 목록 조회
    public List<Item> itemList(){
        return itemRepository.findAll();
    }
    // 특정 상품 조회
    public ItemDto itemView(Long id){
        Optional<Item> result = itemRepository.findById(id);
        Item item = result.orElseThrow();
        ItemDto itemDto = Item.fromEntity(item);

        log.info("itemDto : " + itemDto);

        return itemDto;
    }
    // 특정 유저 상품 조회
    @Transactional
    public List<Item> userItemView(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + userDto.getId()));
        log.info("user.getItems() : " + user.getItems());
        // User에 연결된 아이템 리스트 반환
        return user.getItems();
    }
    // 특정 상품 수정
    public void itemModify(Item item, Long id, MultipartFile file)throws Exception{
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath,fileName);
        file.transferTo(saveFile);

        Item tempItem = itemRepository.findItemById(id);
        tempItem.setName(item.getName());
        tempItem.setPrice(item.getPrice());
        tempItem.setStock(item.getStock());
        //tempItem.setSoldout(item.isSoldout());
        //tempItem.setCount(item.getCount());
        tempItem.setText(item.getText());
        tempItem.setFilename(fileName);
        tempItem.setFilepath("/files/" + fileName);

        itemRepository.save(tempItem);
    }

    // 특정 상품 삭제
    public void itemDelete(Long id){
        itemRepository.deleteById(id);
    }

}
