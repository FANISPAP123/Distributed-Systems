
package com.example.map;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import com.androidplot.ui.Anchor;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.PositionMetrics;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Activity4 extends Activity {
    private XYPlot barPlot;
    private Pair<Integer, XYSeries> selection;
    private BarFormatter selectionFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity4);
        Intent intent = getIntent();
        String dist1 = intent.getStringExtra("newD");
        String ele1 = intent.getStringExtra("newEle");
        String time1 = intent.getStringExtra("newTime");
        String speed1 = intent.getStringExtra("newSpeed");

        // Μετατροπή των δεδομένων σε αριθμητικές τιμές
        double dist2 = Double.parseDouble(dist1);
        double ele2 = Double.parseDouble(ele1);
        double time2 = Double.parseDouble(time1);
        double speed2 = Double.parseDouble(speed1);

        // Εύρεση των TextViews στο layout
        TextView sdistTextView = findViewById(R.id.s5);
        TextView seleTextView = findViewById(R.id.s6);
        TextView stimeTextView = findViewById(R.id.s7);
        TextView speedTextView = findViewById(R.id.s8);

        // Εμφάνιση των ποσοστών στα TextViews
        DecimalFormat decimalFormat = new DecimalFormat("##.##%");
        String distPercentage = decimalFormat.format(dist2);
        String elePercentage = decimalFormat.format(ele2);
        String timePercentage = decimalFormat.format(time2);
        String speedPercentage = decimalFormat.format(speed2);
        if(dist2<0) {
            sdistTextView.setText("Distance " + distPercentage+"⬇️");
        }else{
            sdistTextView.setText("Distance " + distPercentage+"⬆️️");
        }
        if(ele2<0) {
            seleTextView.setText("Elevation " + elePercentage+"⬇️");
        }else{
            seleTextView.setText("Elevation " + elePercentage+"⬆️");
        }
        if(time2<0) {
            stimeTextView.setText("Time " + timePercentage+"⬇️");
        }else{
            stimeTextView.setText("Time " + timePercentage+"⬆️️");
        }
        if(speed2<0) {
            speedTextView.setText("Speed " + speedPercentage+"⬇️");
        }else{
            speedTextView.setText("Speed " + speedPercentage+"⬆️");
        }

        // Εύρεση του γραφήματος ράβδων στο layout
        barPlot = findViewById(R.id.barPlot);
        List<Number> yVals = Arrays.asList(Math.abs(dist2), Math.abs(ele2), Math.abs(time2), Math.abs(speed2));
        List<Number> xVals = Arrays.asList(1, 2, 3, 4);
        List<Number> zVals = Arrays.asList(dist2,ele2,time2,speed2);
        // Δημιουργία της σειράς δεδομένων
        XYSeries series1 = new SimpleXYSeries(xVals, zVals, "");
        XYSeries series = new SimpleXYSeries(xVals, yVals, "");
        // Δημιουργία του φορματέρ για τις ράβδους
        BarFormatter barFormatter = new BarFormatter(Color.rgb(0, 100, 0), Color.LTGRAY) {
            public int getBarPaint(int seriesIndex) {
                Number val = series1.getY(seriesIndex);
                if (val != null && val.doubleValue() < 0) {
                    return Color.RED; // Θέστε το χρώμα των αρνητικών τιμών σε κόκκινο
                }else{
                    return Color.GREEN;
                }
            }
        };

        // Δημιουργία του ραβδογράφηματος
        barPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("0"));
        barPlot.getGraph().setMarginBottom(20);
        barPlot.addSeries(series, barFormatter);

        // Ορισμός των ορίων των αξόνων
        barPlot.setRangeBoundaries(0, BoundaryMode.FIXED, 1, BoundaryMode.FIXED);
        barPlot.setDomainBoundaries(0, BoundaryMode.FIXED, 5, BoundaryMode.FIXED);

        // Προσθήκη της σειράς δεδομένων και του φορματέρ στο γράφημα
        barPlot.addSeries(series, barFormatter);
        barPlot.redraw();
    }

}

