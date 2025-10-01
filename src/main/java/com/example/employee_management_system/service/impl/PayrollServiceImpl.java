package com.example.employee_management_system.service.impl;

import com.example.employee_management_system.entity.Payroll;
import com.example.employee_management_system.repository.PayrollRepository;
import com.example.employee_management_system.service.PayrollService;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.List;

@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollServiceImpl(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    @Override
    public List<Payroll> findAll() {
        return payrollRepository.findAll();
    }

    @Override
    public Payroll findById(Long id) {
        return payrollRepository.findById(id).orElse(null);
    }

    @Override
    public Payroll save(Payroll payroll) {
        // calculate net salary
        double net = payroll.getBasicSalary() + payroll.getAllowances() - payroll.getDeductions();
        payroll.setNetSalary(net);
        return payrollRepository.save(payroll);
    }

    @Override
    public void delete(Long id) {
        payrollRepository.deleteById(id);
    }

    @Override
    public byte[] generatePdf(Payroll payroll) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Payroll Report"));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("Employee: " + payroll.getEmployee().getFirstName() +
                    " " + payroll.getEmployee().getLastName()));
            document.add(new Paragraph("Basic Salary: " + payroll.getBasicSalary()));
            document.add(new Paragraph("Allowances: " + payroll.getAllowances()));
            document.add(new Paragraph("Deductions: " + payroll.getDeductions()));
            document.add(new Paragraph("Net Salary: " + payroll.getNetSalary()));
            document.add(new Paragraph("Pay Date: " + payroll.getPayDate()));

            document.close();
            return baos.toByteArray();

        } catch (DocumentException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
