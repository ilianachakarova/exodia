package chakarova.exodia.web.controllers;

import chakarova.exodia.domain.model.binding.DocumentScheduleBindingModel;
import chakarova.exodia.domain.model.service.DocumentServiceModel;
import chakarova.exodia.domain.model.view.DocumentDetailViewModel;
import chakarova.exodia.domain.model.view.PrintViewModel;
import chakarova.exodia.service.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class DocumentController {

    private final DocumentService documentService;
    private final ModelMapper modelMapper;

    @Autowired
    public DocumentController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/schedule")
    public ModelAndView modelAndView(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("username") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            modelAndView.setViewName("schedule");
        }
        return modelAndView;
    }

    @PostMapping("/schedule")
    public ModelAndView scheduleConfirm(@ModelAttribute DocumentScheduleBindingModel documentScheduleBindingModel, ModelAndView modelAndView) {
        DocumentServiceModel documentServiceModel = this.documentService.
                scheduleDocument(this.modelMapper.map(documentScheduleBindingModel, DocumentServiceModel.class));

        if (documentServiceModel == null) {
            throw new IllegalArgumentException("Something went wrong");
        }
        modelAndView.setViewName("redirect:/details/" + documentServiceModel.getId());
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable(name = "id") String id, ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("username") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {

            DocumentServiceModel documentServiceModel = this.documentService.findDocumentById(id);
            if (documentServiceModel == null) {
                throw new IllegalArgumentException("Something went wrong");
            }

            modelAndView.setViewName("details");
            modelAndView.addObject("model", this.modelMapper.map(documentServiceModel, DocumentDetailViewModel.class));
        }
        return modelAndView;
    }
    @GetMapping("/print/{id}")
    public ModelAndView print(@PathVariable(name = "id")String id, ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username")==null){
            modelAndView.setViewName("redirect:/login");
        }else {
            modelAndView.setViewName("print");
            DocumentServiceModel documentServiceModel = this.documentService.findDocumentById(id);
            PrintViewModel model = this.modelMapper.map(documentServiceModel, PrintViewModel.class);
            modelAndView.addObject("model",model);
        }
        return modelAndView;
    }
    @PostMapping("/print/{id}")
    public ModelAndView confirmPrint(@PathVariable(name = "id")String id, ModelAndView modelAndView){
        if(!this.documentService.printDocumentById(id)){
            throw new IllegalArgumentException("Print Document Failed");
        }else {
            modelAndView.setViewName("redirect:/home");
            return modelAndView;
        }
    }
}
