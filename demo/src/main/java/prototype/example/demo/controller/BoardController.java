package prototype.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prototype.example.demo.Dto.BoardDto;
import prototype.example.demo.service.BoardService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/prototype")
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/write")
    public String write(){
        return "CQ_DBS_002_02";
    }

    @GetMapping(value = "/mi")
    public String miPage() {
        return "CQ_MI_001";
    }

    @PostMapping(value = "/board/submit")
    public String submit(@ModelAttribute BoardDto boardDto, @RequestParam("files") List<MultipartFile> files) throws IOException {
        boardService.save(boardDto, files);
        return "redirect:/prototype/main";
    }
}

