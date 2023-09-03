package com.NhapHocVKUFullStack.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.models.Student;

@Service
public class StudentLoginService {

    private final String API_URL = Utils.BASE_URL+"/api/admin/list";
    private RestTemplate restTemplate = new RestTemplate();

    // login -> numberCIC, birthday
    public Student login(String birthday, String numberCIC) {
        ResponseEntity<Student[]> response = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                null,
                Student[].class
        );

        List<Student> studentList = Arrays.asList(response.getBody());

        // Convert the input birthday to a common format "yyyy-MM-dd"
        String formattedBirthday = convertToCommonDateFormat(birthday);

        Optional<Student> optionalStudent = studentList.stream()
                .filter(student -> {
                    String studentBirthday = convertToCommonDateFormat(student.getBirthday());
                    return studentBirthday.equals(formattedBirthday) && student.getNumberCIC().equals(numberCIC);
                })
                .findFirst();

        return optionalStudent.orElse(null);
    }

    public String convertToCommonDateFormat(String originalDate) {
        // Try parsing the original date as "yyyy-MM-dd"
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = originalFormat.parse(originalDate);

            // Convert it to "yyyy-MM-dd" format
            SimpleDateFormat commonFormat = new SimpleDateFormat("yyyy-MM-dd");
            return commonFormat.format(date);
        } catch (ParseException e) {
            // If parsing as "yyyy-MM-dd" fails, try parsing as "dd/MM/yyyy"
            SimpleDateFormat altFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = altFormat.parse(originalDate);

                // Convert it to "yyyy-MM-dd" format
                SimpleDateFormat commonFormat = new SimpleDateFormat("yyyy-MM-dd");
                return commonFormat.format(date);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Invalid date format. Supported formats: yyyy-MM-dd or dd/MM/yyyy");
            }
        }
    }
}
