package com.brvsk.ZenithActive.pdf;

import com.brvsk.ZenithActive.trainingplan.create.entity.Exercise;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingDay;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingPlan;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;
public class PdfGenerator {

    public static void createTrainingPlanPdf(TrainingPlan trainingPlan) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("training_plan.pdf"));
            document.open();

            // Add member information
            document.add(new Paragraph("Member ID: " + trainingPlan.getMember().getUserId()));

            // Add some space
            addEmptyLine(document, 3);

            // Add instructor information
            document.add(new Paragraph("Instructor ID: " + trainingPlan.getInstructor().getUserId()));

            // Add some space
            addEmptyLine(document, 3);

            // Add training days table
            PdfPTable table = createTrainingDaysTable(trainingPlan.getTrainingDays());
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static PdfPTable createTrainingDaysTable(List<TrainingDay> trainingDays) {
        PdfPTable table = new PdfPTable(8); // 8 columns for days of the week

        // Add header row with empty cell in the first column and days of the week in the rest
        table.addCell(new PdfPCell(new Paragraph("")));
        String[] daysOfWeek = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        for (String day : daysOfWeek) {
            PdfPCell cell = new PdfPCell(new Paragraph(day));
            table.addCell(cell);
        }

        // Add rows with training data
        int weekNumber = 1;
        int trainingDayIndex = 0;

        while (trainingDayIndex < trainingDays.size()) {
            // Week cell in the first column
            PdfPCell weekCell = new PdfPCell(new Paragraph("WEEK " + RomanNumerals.toRoman(weekNumber)));
            table.addCell(weekCell);

            // Training data cells for the whole week
            for (int i = 0; i < 7; i++) {
                if (trainingDayIndex < trainingDays.size()) {
                    TrainingDay trainingDay = trainingDays.get(trainingDayIndex);
                    PdfPCell dayCell = new PdfPCell();

                    // Add training data for the day
                    for (Exercise exercise : trainingDay.getExercises()) {
                        dayCell.addElement(new Paragraph(exercise.toString()));
                    }

                    table.addCell(dayCell);
                    trainingDayIndex++;
                } else {
                    // Add empty cells if there are no more training days
                    table.addCell(new PdfPCell(new Paragraph("")));
                }
            }

            weekNumber++;
        }

        return table;
    }

    private static void addEmptyLine(Document document, int lines) throws DocumentException {
        for (int i = 0; i < lines; i++) {
            document.add(Chunk.NEWLINE);
        }
    }
}
