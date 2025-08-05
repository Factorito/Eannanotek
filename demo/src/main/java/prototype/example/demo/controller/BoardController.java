package prototype.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import prototype.example.demo.Dto.BoardDto;
import prototype.example.demo.service.BoardService;

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

    @PostMapping(value = "/board-submit")
    public String submit(@ModelAttribute BoardDto boardDto){
        boardService.save(boardDto);
        return "redirect:/prototype/main";
    }
}

