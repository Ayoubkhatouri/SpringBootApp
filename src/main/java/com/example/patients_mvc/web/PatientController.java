package com.example.patients_mvc.web;

import com.example.patients_mvc.entities.Patient;
import com.example.patients_mvc.repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor //pour faire l'injection de dependence avec un constructeur avec parametre
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping(path = "/index") //cad que cette methode qui va s'executer lorsqu'on tape http://localhost:8082/index
    public String patients(Model model, @RequestParam(name = "page",defaultValue ="0") int page,
                           @RequestParam(name = "size",defaultValue ="5") int size,
                           @RequestParam(name = "keyword",defaultValue = "" ) String keyword
                           ){
        Page<Patient> pagePatients=patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listPatients",pagePatients.getContent());
        model.addAttribute("pages",new int [pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "patients";//patients c'est une vue
    }

    @GetMapping(path = "/delete")
    public String delete(Long id,String keyword,int page){
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping(path = "/")
    public String home(){
        return "redirect:/index";
    }


    @GetMapping(path = "/patients")
    @ResponseBody
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }

    @GetMapping("/formPatients")
    public String formPatient(Model model){
        model.addAttribute("patient",new Patient());
        return "FormPatients";
    }

    @GetMapping("/save")
    public String save(Model model,Patient patient){
        patientRepository.save(patient);
        return "FormPatients";
    }



}
