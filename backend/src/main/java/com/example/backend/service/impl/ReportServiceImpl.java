package com.example.backend.service.impl;

import com.example.backend.dto.request.ReportRequestDTO;
import com.example.backend.exception.DiagnosisNotFoundException;
import com.example.backend.exception.ExaminationNotFoundException;
import com.example.backend.exception.MedicineNotFoundException;
import com.example.backend.exception.ReportNotFoundException;
import com.example.backend.model.Diagnosis;
import com.example.backend.model.Examination;
import com.example.backend.model.Medicine;
import com.example.backend.model.Report;
import com.example.backend.repository.DiagnosisRepository;
import com.example.backend.repository.ExaminationRepository;
import com.example.backend.repository.MedicineRepository;
import com.example.backend.repository.ReportRepository;
import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;

    private ExaminationRepository examinationRepository;

    private DiagnosisRepository diagnosisRepository;

    private MedicineRepository medicineRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository , ExaminationRepository examinationRepository,
                             DiagnosisRepository diagnosisRepository, MedicineRepository medicineRepository) {
        this.reportRepository = reportRepository;
        this.examinationRepository = examinationRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Report findReportById(Integer id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Could not find report with given id."));
    }

    @Override
    public Report createReport(Integer id, ReportRequestDTO reportRequestDTO) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ExaminationNotFoundException("COuld not find examination with given id"));
        Report report = new Report();
        reportRepository.save(report);
        System.out.println(report.getReportId() + " aaaaaaaaaaaaa");
        report.setExamination(examination);
        report.setDescription(reportRequestDTO.getDescription());
        report.setDiagnoses(new ArrayList<>());
        report.setMedicines(new ArrayList<>());

        if(reportRequestDTO.getDiagnoses() != null) {
            for (Integer diagnosis : reportRequestDTO.getDiagnoses()) {
                Diagnosis d = diagnosisRepository.findById(diagnosis)
                        .orElseThrow(() -> new DiagnosisNotFoundException("Could not find diagnosis with given id"));
            report.getDiagnoses().add(d);
            }
        }

        if(reportRequestDTO.getMedicines() != null) {
            for (Integer medicine : reportRequestDTO.getMedicines()) {
                Medicine m = medicineRepository.findById(medicine)
                        .orElseThrow(() -> new MedicineNotFoundException("Could not find medicine with given id"));
                report.getMedicines().add(m);
            }
        }

        reportRepository.save(report);
        examination.setFinished(true);
        examinationRepository.save(examination);
        return report;
    }
}
