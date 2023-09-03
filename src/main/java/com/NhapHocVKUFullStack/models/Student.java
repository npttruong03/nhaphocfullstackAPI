package com.NhapHocVKUFullStack.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    private String id;
    private String numberCIC;
    private Majors majors;
	private DocumentItems docuItems;
    private Khoa khoa;
    private Curriculum curriculum;
    public Method method;
    private String fullName;
    private String birthday;
    private String birthplace;
    private String gender;
    private String homeTown;
    private String ethnic;
    private String phoneNumber;
    private String email;
    private boolean unionMember;
    private String treatmentPolicy;
    private String documentItems;
    private String phoneNbHome;
    private String proofOfAdmission;
    private Byte registerSession;
    private String graduationYear;
    private String highSchool;
    private String idHighSchool;
    private String school10;
    private String school11;
    private String school12;
    private boolean status;
    private String createTime;
    private String updateTime;
    private String dateIssuanceCIC;
    private String placeIssuanceCIC;
    private String className;
    private String idStudent;
	private String sbd;
	private Double diemTrungTuyen;
	private String toHopMon;
	private Boolean trangThaiNhapHoc;
	private String emailvku;
	private String religion;
	private String cuTruTinh;
	private String cuTruQuanHuyen;
	private String cuTruXaPhuong;
	private String cuTruToThon;
	private String codeBHYT;
	private String area;
	private String priorityObject;
	private String noteDocumentItems;
	private Boolean emailSended;
	private String provinceSchool;
}