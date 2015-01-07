package th.jack.jirawuts.miniapartment;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CancellationSignal;
import android.os.Bundle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.content.Context;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.graphics.pdf.PdfDocument;
import android.widget.Toast;

/**
 * Created by Jirawut-Jack on 30/12/2014.
 */

public class PrintModule extends PrintDocumentAdapter {

    //Explicit
    Context context;
    private int pageHeight;
    private int pageWidth;

    private Integer intMonth, intYear, intTotalPages;
    private String strMonth;
    public PdfDocument myPdfDocument;

    private AptTABLE objAptTABLE;
    private RoomTABLE objRoomTABLE;
    private RentalTABLE objRentalTABLE;

    private String[] arrayRoom;
    private DecimalFormat decFormat;

    public PrintModule(Context context, String[] array, Integer pMonth, String pMonthName, Integer pYear) {
        this.context = context;
        arrayRoom = array;
        intTotalPages = arrayRoom.length;
        intMonth = pMonth;
        intYear = pYear;
        strMonth = pMonthName;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {

        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight =
                newAttributes.getMediaSize().getHeightMils() / 1000 * 72;

        pageWidth =
                newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        if (intTotalPages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder(strMonth + intYear.toString() + ".pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(intTotalPages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal
                                cancellationSignal,
                        final WriteResultCallback callback) {

        for (int i = 0; i < intTotalPages; i++) {
//            if (pageInRange(pageRanges, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i, arrayRoom[i]);
                myPdfDocument.finishPage(page);
//            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }
        callback.onWriteFinished(pageRanges);

    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    // drawPage
    private void drawPage(PdfDocument.Page page,
                          int intPageNo, String strNo) {

        Canvas canvas = page.getCanvas();
        decFormat = new DecimalFormat("#,###");

        Integer intY = 35;
        Integer intRowSpaceBig = 25;
        Integer intRowSpaceSmall = 15;
        Integer intFontBig = 14;
        Integer intFontNormal = 12;
        Integer intMargin = 20;
        Integer intTableHeight = 175;
        Integer intTableWidth = canvas.getWidth() - (2 * intMargin);
        Integer intColumnWidth = intTableWidth / 4;
        Integer intTableMargin = 10;
        Double dblTotal = 0.00;

        intPageNo++; // Make sure page numbers start at 1

        objAptTABLE = new AptTABLE(this.context);
        objRoomTABLE = new RoomTABLE(this.context);
        objRentalTABLE = new RentalTABLE(this.context);

        String[] arrayApt = objAptTABLE.searchApt(1);
        String[] arrayRoom = objRoomTABLE.searchRoom(strNo);
        String[] arrayRental = objRentalTABLE.searchRental(this.context, strNo, intMonth, intYear);

        dblTotal = Double.parseDouble(arrayRental[12]) + Double.parseDouble(arrayRental[11]) +
                Double.parseDouble(arrayRental[7]) + Double.parseDouble(arrayRental[13]) +
                Double.parseDouble(arrayRental[14]) + Double.parseDouble(arrayRental[15]);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        for (int intC = 0; intC <= 1; intC++) {

            //Print Apartment Name
            paint.setTextSize(intFontBig);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setUnderlineText(true);
            paint.setFakeBoldText(true);
            canvas.drawText(
                    arrayApt[1],
                    canvas.getWidth() / 2, intY,
                    paint);

            //Print form header
            intY = intY + intRowSpaceBig;
            paint.setTextSize(intFontNormal);
            paint.setUnderlineText(false);
            paint.setFakeBoldText(true);
            canvas.drawText(
                    "ใบแจ้งการชำระค่าเช่า",
                    canvas.getWidth() / 2, intY,
                    paint);

            //Print Due date, Room Number & Receipt date
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("งวดชำระ " + strMonth + " " + String.valueOf(intYear),
                    intMargin, intY, paint);

            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("ห้องพักเลขที่ " + strNo,
                    canvas.getWidth() / 2, intY, paint);

            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("วันที่ชำระ__________________", canvas.getWidth() - intMargin, intY, paint);

            //Print Payment Detail Table
            intY = intY + intRowSpaceSmall;
            //แนวนอนเส้นบน
            canvas.drawLine(intMargin, intY, canvas.getWidth() - intMargin, intY, paint);
            canvas.drawLine(intMargin, intY + intRowSpaceBig, canvas.getWidth() - intMargin, intY + intRowSpaceBig, paint);
            //แนวนอนเส้นล่าง
            canvas.drawLine(intMargin, intY + intTableHeight, canvas.getWidth() - intMargin, intY + intTableHeight, paint);
            //เส้นแนวตั้งซ้าย
            canvas.drawLine(intMargin, intY, intMargin, intY + intTableHeight, paint);
            //เส้นแนวตั้งขวา
            canvas.drawLine(canvas.getWidth() - intMargin, intY, canvas.getWidth() - intMargin, intY + intTableHeight, paint);
            //เส้นกั้น Column
            canvas.drawLine(intMargin + intColumnWidth, intY, intMargin + intColumnWidth, intY + intTableHeight, paint);
            canvas.drawLine(intMargin + (intColumnWidth * 2), intY, intMargin + (intColumnWidth * 2), intY + intTableHeight, paint);
            canvas.drawLine(intMargin + (intColumnWidth * 3), intY, intMargin + (intColumnWidth * 3), intY + intTableHeight, paint);

            //Print Table Header
            intY = intY + intRowSpaceSmall;
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("รายการ", intMargin + (intColumnWidth / 2), intY, paint);
            canvas.drawText("จำนวน Unit ต้นเดือน", intMargin + intColumnWidth + (intColumnWidth / 2), intY, paint);
            canvas.drawText("จำนวน Unit ปลายเดือน", intMargin + (intColumnWidth * 2) + (intColumnWidth / 2), intY, paint);
            canvas.drawText("จำนวนเงิน", intMargin + (intColumnWidth * 3) + (intColumnWidth / 2), intY, paint);

            //Print Rental Detail
            paint.setFakeBoldText(false);
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("ค่าเช่าและค่าบริการ", intMargin + intTableMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[12].toString())).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Electric
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("ค่าน้ำ", intMargin + intTableMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[8].toString())).toString(), ((intColumnWidth * 2) + intMargin) - intTableMargin, intY, paint);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[9].toString())).toString(), ((intColumnWidth * 3) + intMargin) - intTableMargin, intY, paint);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[11].toString())).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Water
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("ค่าไฟ", intMargin + intTableMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[4].toString())).toString(), ((intColumnWidth * 2) + intMargin) - intTableMargin, intY, paint);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[5].toString())).toString(), ((intColumnWidth * 3) + intMargin) - intTableMargin, intY, paint);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[7].toString())).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Phone
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("ค่าโทรศัพท์", intMargin + intTableMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[14].toString())).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Internet
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("ค่า Internet", intMargin + intTableMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[13].toString())).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Other
            intY = intY + intRowSpaceBig;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("อื่นๆ", intMargin + intTableMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(decFormat.format(Double.parseDouble(arrayRental[15].toString())).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Total
            paint.setFakeBoldText(true);
            intY = intY + intRowSpaceBig + 5;
            canvas.drawText("รวมทั้งสิ้น  " + decFormat.format(dblTotal).toString(), (intTableWidth + intMargin) - intTableMargin, intY, paint);

            //Print Note & Receipt person
            intY = intY + intRowSpaceBig + 15;
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(intFontNormal);
            canvas.drawText("หมายเหตุ : กำหนดชำระเงินไม่เกินวันที่ 5 ของทุกเดือน", intMargin, intY, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("ผู้รับเงิน__________________", canvas.getWidth() - intMargin, intY, paint);

            //Draw separator line
            if (intC == 0) {
                intY = intY + intRowSpaceBig + 10;
                canvas.drawLine(intMargin, intY,canvas.getWidth() - intMargin,intY, paint);

                //Set next Y
                intY = intY + intRowSpaceBig + 10;
            }
        }

    }

}
