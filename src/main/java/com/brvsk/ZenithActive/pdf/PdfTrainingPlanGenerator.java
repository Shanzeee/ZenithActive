package com.brvsk.ZenithActive.pdf;

import com.brvsk.ZenithActive.user.member.Member;
import com.brvsk.ZenithActive.trainingplan.create.entity.Exercise;
import com.brvsk.ZenithActive.trainingplan.create.entity.ExerciseType;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingDay;
import com.brvsk.ZenithActive.trainingplan.create.entity.TrainingPlan;
import com.brvsk.ZenithActive.trainingplan.request.entity.TrainingPlanRequest;
import com.brvsk.ZenithActive.utils.RomanNumerals;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
public class PdfTrainingPlanGenerator {

    public static void createTrainingPlanPdf(TrainingPlan trainingPlan, String outputFolder, String fileName) {
        Document document = new Document();

        try {
            Path userFolder = Paths.get(outputFolder, trainingPlan.getMember().getUserId().toString());
            Files.createDirectories(userFolder);

            Path filePath = userFolder.resolve(fileName + ".pdf");

            PdfWriter.getInstance(document, new FileOutputStream(filePath.toString()));
            document.open();

            addTitle(document);

            addMemberInfoParagraph(document, trainingPlan);

            addInstructorInfoParagraph(document, trainingPlan);

            addEmptyLine(document, 1);

            PdfPTable table = createTrainingDaysTable(trainingPlan.getTrainingDays());
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static PdfPTable createTrainingDaysTable(List<TrainingDay> trainingDays) {
        PdfPTable table = new PdfPTable(8);

        Font dayOfWeekFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        Font weekNumberFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font exerciseFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

        // Set cell properties for the table
        table.setWidthPercentage(98);
        table.getDefaultCell().setPadding(5);
        table.getDefaultCell().setBorderWidth(1f);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        // Add an empty cell in the first column
        PdfPCell emptyCell = new PdfPCell(new Paragraph(""));
        table.addCell(emptyCell);

        String[] daysOfWeek = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        for (String day : daysOfWeek) {
            PdfPCell cell = new PdfPCell(new Paragraph(day, dayOfWeekFont));
            cell.setBackgroundColor(new BaseColor(0, 150, 0));  // Set background color to dark green
            table.addCell(cell);
        }

        int weekNumber = 1;
        int trainingDayIndex = 0;

        while (trainingDayIndex < trainingDays.size()) {
            // Week cell in the first column
            PdfPCell weekCell = new PdfPCell(new Paragraph("WEEK " + RomanNumerals.toRoman(weekNumber), weekNumberFont));
            weekCell.setBackgroundColor(new BaseColor(0,190,90));
            table.addCell(weekCell);

            // Training data cells for the whole week
            for (int i = 0; i < 7; i++) {
                if (trainingDayIndex < trainingDays.size()) {
                    TrainingDay trainingDay = trainingDays.get(trainingDayIndex);
                    PdfPCell dayCell = new PdfPCell();

                    for (Exercise exercise : trainingDay.getExercises()) {
                        dayCell.addElement(new Paragraph(exercise.toString(), exerciseFont));
                    }

                    if (trainingDay.getExercises().stream().anyMatch(e -> e.getExerciseType() == ExerciseType.REST_DAY)) {
                        dayCell.setBackgroundColor(BaseColor.GREEN);
                    }

                    table.addCell(dayCell);
                    trainingDayIndex++;
                } else {
                    // Add empty cells if there are no more training days
                    PdfPCell emptyCellInRow = new PdfPCell(new Paragraph("", exerciseFont));
                    table.addCell(emptyCellInRow);
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

    private static void addMemberInfoParagraph(Document document, TrainingPlan trainingPlan) throws DocumentException {
        Member member = trainingPlan.getMember();
        TrainingPlanRequest trainingPlanRequest = trainingPlan.getTrainingPlanRequest();

        Paragraph memberParagraph = new Paragraph(
                member.getFirstName() + " " + member.getLastName() +"\n"+
                        "target: " + trainingPlanRequest.getMemberInfo().getTarget() + "\n" +
                        "body fat: " + trainingPlanRequest.getMemberInfo().getBodyFatLevel() + "% \n" +
                        "weight: " + trainingPlanRequest.getMemberInfo().getCurrentWeight() + "kg -> " + trainingPlanRequest.getMemberInfo().getTargetWeight() +"kg"
        );
        memberParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(memberParagraph);
    }

    private static void addTitle(Document document) throws DocumentException {
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Paragraph titleParagraph = new Paragraph("TRAINING PLAN", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);
    }

    private static void addInstructorInfoParagraph(Document document, TrainingPlan trainingPlan) throws DocumentException {
        Paragraph instructorIdParagraph = new Paragraph(
                "MADE BY: " + trainingPlan.getInstructor().getFirstName() + " " + trainingPlan.getInstructor().getLastName()
        );
        instructorIdParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(instructorIdParagraph);
    }
}
