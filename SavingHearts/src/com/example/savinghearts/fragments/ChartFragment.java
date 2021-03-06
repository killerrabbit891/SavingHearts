package com.example.savinghearts.fragments;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.savinghearts.*;
import com.example.savinghearts.helpers.CalculationsHelper;
import com.example.savinghearts.model.ActivityData;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class ChartFragment extends Fragment{
	 private GraphicalView mChartView, mChartView2, mChartView3;
	 SavingHeartsDataSource db;

	public static final String ARG_SECTION_NUMBER = "section_number";

	public ChartFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =	inflater.inflate(R.layout.bar_plot, container, false);
		//Button searchBeat = (Button) view.findViewById(R.id.searchMonitor);
		//searchBeat.setOnClickListener(this);	 
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();

		drawChart();
	}
	
	public void drawChart(){
		 db = SavingHeartsDataSource.getInstance(this.getActivity());
        if(db.getActivityDataCount() !=0){
	        	List<ActivityData> activities = db.getAllActivitiesInPast7Days();
		       
		       ArrayList<String> xDates = getLast7DaysDate();
		        
		      /*
		       * Heart Rate List
		       */
		        ArrayList<Integer> yMaxHR = new ArrayList<Integer>();
		        ArrayList<Integer> maxHR = new ArrayList<Integer>();
		        HashMap<Integer,Integer> dateHR = new HashMap<Integer,Integer>();
		        ArrayList<Integer> dates = new ArrayList<Integer>();
		        /*
		         * METs List
		         */
		        ArrayList<Double> yMaxMETs = new ArrayList<Double>();
		        ArrayList<Double> maxMETs = new ArrayList<Double>();
		        HashMap<Integer,Double> dateMETs = new HashMap<Integer,Double>();
		        ArrayList<Integer> datesMETs = new ArrayList<Integer>();
		        /*
		         * Calories List
		         */
		        ArrayList<Double> yMaxCalories = new ArrayList<Double>();
		        ArrayList<Double> maxCalories = new ArrayList<Double>();
		        HashMap<Integer,Double> dateCalories = new HashMap<Integer,Double>();
		        ArrayList<Integer> datesCalories = new ArrayList<Integer>();
		       
		       
		        /*
		         * compare each activities in a list
		         * if they have the same timestamp then compare the max HR and insert it to y
		         * if they have different timestamp go to the next list of acivities
		         * also get the dates
		         * 
		         */
		        //j is the activities
		        //k is the ymaxHR
		        int j=0,k=0;
		        int timestamp = Integer.parseInt(activities.get(j).getDate()); 
		       
		        yMaxHR.add(activities.get(j).getMaxHR());
		        dateHR.put(timestamp, activities.get(j).getMaxHR());
		        dates.add(timestamp);
		        while(j != activities.size() ){
		        	if(j+1 != activities.size()){
			        	if(timestamp==Integer.parseInt(activities.get(j+1).getDate())){
			        		if(yMaxHR.get(k) < activities.get(j+1).getMaxHR()){
			        			yMaxHR.set(k, activities.get(j+1).getMaxHR());
			        			dateHR.remove(timestamp);
			        			dateHR.put(timestamp, activities.get(j+1).getMaxHR());
			        		}
			        	}
			        	else {
			        		timestamp = Integer.parseInt(activities.get(j+1).getDate());
			        		k++;
			        		yMaxHR.add(activities.get(j+1).getMaxHR());
			        		dateHR.put(timestamp, activities.get(j+1).getMaxHR());
			        		dates.add(timestamp);
			        	} 	
		        	}
		        	j++;
		        }
		        /*
		         * get max HR in 7 days
		         */
		        int maxHRin7Days = yMaxHR.get(0);
		        for(int i =1; i< yMaxHR.size();i++){
		        	if(maxHRin7Days < yMaxHR.get(i)){
		        		maxHRin7Days = yMaxHR.get(i);
		        	}
		        }
		        
		        
		        
		        /*
		         * compare each activities in a list
		         * if they have the same timestamp then compare the max METs and insert it to y
		         * if they have different timestamp go to the next list of acivities
		         * also get the dates
		         * 
		         */
		        //j2 is the activities
		        //k2 is the ymaxMETs
		        int j2=0,k2=0;
		        int timestamp2 = Integer.parseInt(activities.get(j2).getDate()); 
		       
		        yMaxMETs.add(activities.get(j2).getMets());
		        dateMETs.put(timestamp2, activities.get(j2).getMets());
		        datesMETs.add(timestamp2);
		        while(j2 != activities.size() ){
		        	if(j2+1 != activities.size()){
			        	if(timestamp2==Integer.parseInt(activities.get(j2+1).getDate())){
			        		if( yMaxMETs.get(k2) < activities.get(j2+1).getMets()){
			        			 yMaxMETs.set(k2, activities.get(j2+1).getMets());
			        			 dateMETs.remove(timestamp2);
			        			 dateMETs.put(timestamp2, activities.get(j2+1).getMets());
			        		}
			        	}
			        	else {
			        		timestamp2 = Integer.parseInt(activities.get(j2+1).getDate());
			        		k2++;
			        		 yMaxMETs.add(activities.get(j2+1).getMets());
			        		 dateMETs.put(timestamp2, activities.get(j2+1).getMets());
			        		 datesMETs.add(timestamp2);
			        	} 	
		        	}
		        	j2++;
		        }
		        /*
		         * get max METs in 7 days
		         */
		        
		        Double maxMETSin7Days = yMaxMETs.get(0);
		        for(int i =1; i< yMaxMETs.size();i++){
		        	if(maxMETSin7Days < yMaxMETs.get(i)){
		        		maxMETSin7Days = yMaxMETs.get(i);
		        	}
		        }
		        
		        /*
		         * compare each activities in a list
		         * if they have the same timestamp then compare the max Calories and insert it to y
		         * if they have different timestamp go to the next list of activities
		         * also get the dates
		         * 
		         */
		        //j2 is the activities
		        //k2 is the ymaxMETs
		        int j3=0,k3=0;
		        int timestamp3 = Integer.parseInt(activities.get(j3).getDate()); 
		       
		        yMaxCalories.add(activities.get(j3).getCalories());
		        dateCalories.put(timestamp3, activities.get(j3).getCalories());
		        datesCalories.add(timestamp3);
		        while(j3 != activities.size() ){
		        	if(j3+1 != activities.size()){
			        	if(timestamp3==Integer.parseInt(activities.get(j3+1).getDate())){
			        		if( yMaxCalories.get(k3) < activities.get(j3+1).getCalories()){
			        			yMaxCalories.set(k3, activities.get(j3+1).getCalories());
			        			dateCalories.remove(timestamp3);
			        			dateCalories.put(timestamp3, activities.get(j3+1).getCalories());
			        		}
			        	}
			        	else {
			        		timestamp3 = Integer.parseInt(activities.get(j3+1).getDate());
			        		k3++;
			        		yMaxCalories.add(activities.get(j3+1).getCalories());
			        		dateCalories.put(timestamp3, activities.get(j3+1).getCalories());
			        		datesCalories.add(timestamp3);
			        	} 	
		        	}
		        	j3++;
		        }
		        /*
		         * 
		         */
		        Double maxCaloriesin7Days = yMaxCalories.get(0);
		        for(int i =1; i< yMaxCalories.size();i++){
		        	if(maxCaloriesin7Days < yMaxCalories.get(i)){
		        		maxCaloriesin7Days = yMaxCalories.get(i);
		        	}
		        }
		        
		        //match the dates for HR
		        int datesize =7;
		        int l=6;//the dates starts at the current timestamp (index 0)
		        int m=0;
		        while(datesize!=0){
		        	if(dates.size() != m){
			        	if(Integer.parseInt(xDates.get(l)) == dates.get(m)){
			        		maxHR.add(dateHR.get(dates.get(m)));
			        		m++;
			        	}
			        	else{
			        		maxHR.add(0);
			        	}
		        	}else{
		        		maxHR.add(0);
		        	}
		        	l--;
		        	datesize--;
		        }
		        
		        //match the dates for mets
		        NumberFormat formatter = new DecimalFormat("#0.0"); 
		        int datesize2 =7;
		        int l2=6;
		        int m2=0;
		        while(datesize2!=0){
		        	if(datesMETs.size() != m2){
			        	if(Integer.parseInt(xDates.get(l2)) == datesMETs.get(m2)){
			        		maxMETs.add(Double.parseDouble(formatter.format(dateMETs.get(datesMETs.get(m2)))));
			        		m2++;
			        	}
			        	else{
			        		maxMETs.add(0.0);
			        	}
		        	}else{
		        		maxMETs.add(0.0);
		        	}
		        	l2--;
		        	datesize2--;
		        }
		        
		      //match the dates for calories
		        formatter = new DecimalFormat("#0.0"); 
		        int datesize3 =7;
		        int l3=6;
		        int m3=0;
		        while(datesize3!=0){
		        	if(datesCalories.size() != m3){
			        	if(Integer.parseInt(xDates.get(l3)) == datesCalories.get(m3)){
			        		maxCalories.add(Double.parseDouble(formatter.format(dateCalories.get(datesCalories.get(m3)))));
			        		m3++;
			        	}
			        	else{
			        		maxCalories.add(0.0);
			        	}
		        	}else{
		        		maxCalories.add(0.0);
		        	}
		        	l3--;
		        	datesize3--;
		        }
		        
		        
		        //get y for HR
		        int[] y = new int[maxHR.size()];
		        int s=0;
		        for(int c=0; c < maxHR.size(); c++){
		        	y[c]= maxHR.get(s);
		        	s++;
		        }
		        
		        //get y for METs
		        Double[] y2 = new Double[maxMETs.size()];
		        int s2=0;
		        for(int c=0; c < maxMETs.size(); c++){
		        	y2[c]= maxMETs.get(s2);
		        	s2++;
		        }
		        
		      //get y for Calories
		        Double[] y3 = new Double[maxCalories.size()];
		        int s3=0;
		        for(int c=0; c < maxCalories.size(); c++){
		        	y3[c]= maxCalories.get(s3);
		        	s3++;
		        }
		 
		        CategorySeries series = new CategorySeries("Max Heart Rate");
		        for(int i=0; i < y.length; i++){
		            series.add("Max Heart Rate"+(i+1),y[i]);
		        }
		        
		        CategorySeries series2 = new CategorySeries("Max METs");
		        for(int i=0; i < y2.length; i++){
		            series2.add("Max METs"+(i+1),y2[i]);
		        }
		        
		        CategorySeries series3 = new CategorySeries("Max Calories");
		        for(int i=0; i < y3.length; i++){
		            series3.add("Max Calories"+(i+1),y3[i]);
		        }
		       
		        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
		        dataSet.addSeries(series.toXYSeries());
		        
		        XYMultipleSeriesDataset dataSet2 = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
		        dataSet2.addSeries(series2.toXYSeries());
		        
		        XYMultipleSeriesDataset dataSet3 = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
		        dataSet3.addSeries(series3.toXYSeries());
		        
		        //customization of the chart
		    
		        XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series
		        renderer.setColor(Color.RED);
		        renderer.setDisplayChartValues(true);
		        renderer.setChartValuesTextSize(30f);
		        renderer.setChartValuesSpacing((float) 5.5d);
		        renderer.setLineWidth((float) 10.5d);
		        
		        XYSeriesRenderer renderer2 = new XYSeriesRenderer();     // one renderer for one series
		        renderer2.setColor(Color.RED);
		        renderer2.setDisplayChartValues(true);
		        renderer2.setChartValuesTextSize(30f);
		        renderer2.setChartValuesSpacing((float) 5.5d);
		        renderer2.setLineWidth((float) 10.5d);
		        
		        XYSeriesRenderer renderer3 = new XYSeriesRenderer();     // one renderer for one series
		        renderer3.setColor(Color.RED);
		        renderer3.setDisplayChartValues(true);
		        renderer3.setChartValuesTextSize(30f);
		        renderer3.setChartValuesSpacing((float) 5.5d);
		        renderer3.setLineWidth((float) 10.5d);
		
		        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
		        mRenderer.addSeriesRenderer(renderer);
		        
		        mRenderer.setChartTitle("Max Heart Rate");
		        mRenderer.setAxisTitleTextSize(50f);
		        mRenderer.setXTitle(getCurrentMonth()+" "+getCurrentYear());
		        mRenderer.setYTitle("Heart Rate");
		        mRenderer.setZoomButtonsVisible(false);    mRenderer.setShowLegend(true);
		        mRenderer.setShowGridX(true);      // this will show the grid in  graph
		        mRenderer.setShowGridY(true);              
		//        mRenderer.setAntialiasing(true);
		        mRenderer.setBarSpacing(.5);   // adding spacing between the line or stacks
		        mRenderer.setApplyBackgroundColor(true);
		        mRenderer.setBackgroundColor(Color.WHITE);
		        mRenderer.setMarginsColor(Color.GRAY);
		        mRenderer.setXAxisMin(0);
		//        mRenderer.setYAxisMin(.5);
		        mRenderer.setXAxisMax(8);
		        mRenderer.setYAxisMax(maxHRin7Days+10);
		        mRenderer.setYAxisMin(50);
		        mRenderer.setXLabelsColor(Color.BLACK);
		        mRenderer.setYLabelsColor(0, Color.BLACK);
		        mRenderer.setChartTitleTextSize(50);
		        mRenderer.setLabelsTextSize(50);
		        mRenderer.setLegendTextSize(30);
		        mRenderer.setMargins(new int[]{70,70,70,70});  
		        mRenderer.setInScroll(true);
		//    
		        /*
		         * get date size
		         * get put date
		         */      
		        mRenderer.setXLabels(0);
		        mRenderer.addXTextLabel(1,xDates.get(6));		    
		        mRenderer.addXTextLabel(2,xDates.get(5));
		        mRenderer.addXTextLabel(3,xDates.get(4));
		        mRenderer.addXTextLabel(4,xDates.get(3));
		        mRenderer.addXTextLabel(5,xDates.get(2));
		        mRenderer.addXTextLabel(6,xDates.get(1));
		        mRenderer.addXTextLabel(7,xDates.get(0));
		        
		        
		        mRenderer.setLabelsTextSize(20);
		        mRenderer.setPanEnabled(false, false);    // will fix the chart position
		        mRenderer.setZoomRate(0.2f);
		        mRenderer.setZoomEnabled(false, false);
		        
		        XYMultipleSeriesRenderer mRenderer2 = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
		        mRenderer2.addSeriesRenderer(renderer2);
		        
		        mRenderer2.setChartTitle("Max METs");
		        mRenderer2.setAxisTitleTextSize(50f);
		        mRenderer2.setXTitle(getCurrentMonth()+" "+getCurrentYear());
		        mRenderer2.setYTitle("METs");
		        mRenderer2.setZoomButtonsVisible(false);    mRenderer2.setShowLegend(true);
		        mRenderer2.setShowGridX(true);      // this will show the grid in  graph
		        mRenderer2.setShowGridY(true);              
		//        mRenderer.setAntialiasing(true);
		        mRenderer2.setBarSpacing(.5);   // adding spacing between the line or stacks
		        mRenderer2.setApplyBackgroundColor(true);
		        mRenderer2.setBackgroundColor(Color.WHITE);
		        mRenderer2.setMarginsColor(Color.GRAY);
		        mRenderer2.setXAxisMin(0);
		//        mRenderer.setYAxisMin(.5);
		        mRenderer2.setXAxisMax(8);
		        mRenderer2.setYAxisMax(maxMETSin7Days + 1);
		        mRenderer2.setYAxisMin(0);
		        mRenderer2.setXLabelsColor(Color.BLACK);
		        mRenderer2.setYLabelsColor(0, Color.BLACK);
		        mRenderer2.setChartTitleTextSize(50);
		        mRenderer2.setLabelsTextSize(50);
		        mRenderer2.setLegendTextSize(30);
		        mRenderer2.setMargins(new int[]{70,70,70,70}); 
		        mRenderer2.setInScroll(true);
		//    
		        /*
		         * get date size
		         * get put date
		         */      
		        mRenderer2.setXLabels(0);
		        mRenderer2.addXTextLabel(1,xDates.get(6));
		        mRenderer2.addXTextLabel(2,xDates.get(5));
		        mRenderer2.addXTextLabel(3,xDates.get(4));
		        mRenderer2.addXTextLabel(4,xDates.get(3));
		        mRenderer2.addXTextLabel(5,xDates.get(2));
		        mRenderer2.addXTextLabel(6,xDates.get(1));
		        mRenderer2.addXTextLabel(7,xDates.get(0));
		        
		        
		        mRenderer2.setLabelsTextSize(20);
		        mRenderer2.setPanEnabled(false, false);    // will fix the chart position
		        mRenderer2.setZoomRate(0.2f);
		        mRenderer2.setZoomEnabled(false, false);
		        
		        XYMultipleSeriesRenderer mRenderer3 = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
		        mRenderer3.addSeriesRenderer(renderer3);
		        
		        mRenderer3.setChartTitle("Max Calories");
		        mRenderer3.setAxisTitleTextSize(50f);
		        mRenderer3.setXTitle(getCurrentMonth()+" "+getCurrentYear());
		        mRenderer3.setYTitle("Calories");
		        mRenderer3.setZoomButtonsVisible(false);    mRenderer3.setShowLegend(true);
		        mRenderer3.setShowGridX(true);      // this will show the grid in  graph
		        mRenderer3.setShowGridY(true);              
		//        mRenderer.setAntialiasing(true);
		        mRenderer3.setBarSpacing(.5);   // adding spacing between the line or stacks
		        mRenderer3.setApplyBackgroundColor(true);
		        mRenderer3.setBackgroundColor(Color.WHITE);
		        mRenderer3.setMarginsColor(Color.GRAY);
		        mRenderer3.setXAxisMin(0);
		//        mRenderer.setYAxisMin(.5);
		        mRenderer3.setXAxisMax(8);
		        mRenderer3.setYAxisMax(maxCaloriesin7Days + 20);
		        mRenderer3.setYAxisMin(50);
		        mRenderer3.setXLabelsColor(Color.BLACK);
		        mRenderer3.setYLabelsColor(0, Color.BLACK);
		        mRenderer3.setChartTitleTextSize(50);
		        mRenderer3.setLabelsTextSize(50);
		        mRenderer3.setLegendTextSize(30);
		        mRenderer3.setMargins(new int[]{70,70,70,70}); 
		        mRenderer3.setInScroll(true);
		//    
		        /*
		         * get date size
		         * get put date
		         */      
		        mRenderer3.setXLabels(0);
		        mRenderer3.addXTextLabel(1,xDates.get(6));
		        mRenderer3.addXTextLabel(2,xDates.get(5));
		        mRenderer3.addXTextLabel(3,xDates.get(4));
		        mRenderer3.addXTextLabel(4,xDates.get(3));
		        mRenderer3.addXTextLabel(5,xDates.get(2));
		        mRenderer3.addXTextLabel(6,xDates.get(1));
		        mRenderer3.addXTextLabel(7,xDates.get(0));
		        
		        
		        mRenderer3.setLabelsTextSize(20);
		        mRenderer3.setPanEnabled(false, false);    // will fix the chart position
		        mRenderer3.setZoomRate(0.2f);
		        mRenderer3.setZoomEnabled(false, false);
		      
		       
		        //
		        
		        LinearLayout layout = (LinearLayout)  getActivity().findViewById(R.id.chart);
		        LinearLayout layout2 = (LinearLayout) getActivity().findViewById(R.id.chart2);
		        LinearLayout layout3 = (LinearLayout) getActivity().findViewById(R.id.chart3);
		        mChartView = ChartFactory.getBarChartView(getActivity(), dataSet, mRenderer, Type.DEFAULT);
		        layout.addView(mChartView, new LayoutParams
		    (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		        mChartView2 = ChartFactory.getBarChartView(getActivity(), dataSet2, mRenderer2, Type.DEFAULT);
		        layout2.addView(mChartView2, new LayoutParams
		    (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		        mChartView3 = ChartFactory.getBarChartView(getActivity(), dataSet3, mRenderer3, Type.DEFAULT);
		        layout3.addView(mChartView3, new LayoutParams
		    (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        }
		
	}
	
    
	  //get activity's current date
	  	public String getCurrentDate() {
	          SimpleDateFormat dateFormat = new SimpleDateFormat(
	                  "dd", Locale.getDefault());
	          Date date = new Date();
	          return dateFormat.format(date);
	  	}
	  	
	  	public ArrayList<String> getLast7DaysDate(){
	  		ArrayList<String> dates= new ArrayList<String>();
	  		dates.add(getCurrentDate());
	  		//get yesterday
	  		
	  		for(int i=1; i<8; i++){
	  			Calendar theStart = Calendar.getInstance();
	  	  		SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
				theStart.add(Calendar.DAY_OF_MONTH, -i);			
				String yesterday = dateFormat.format(theStart.getTime());
				dates.add(yesterday);
	  		}
	  		return dates;
	  		
	  	}
	  	
	  	//get activity's current month
	  	public String getCurrentMonth() {
	          SimpleDateFormat dateFormat = new SimpleDateFormat(
	                  "MM", Locale.getDefault());
	          Date date = new Date();
	          String monthNumber = dateFormat.format(date);
	          String month="";
	          if(monthNumber.equals("01")){
	        	  month="January";
	          }
	          else if(monthNumber.equals("02")){
	        	  month="February";
	          }
	          else if(monthNumber.equals("03")){
	        	  month="March";
	          }
	          else if(monthNumber.equals("04")){
	        	  month="April";
	          }
	          else if(monthNumber.equals("05")){
	        	  month="May";
	          }
	          else if(monthNumber.equals("06")){
	        	  month="June";
	          }
	          else if(monthNumber.equals("07")){
	        	  month="July";
	          }
	          else if(monthNumber.equals("08")){
	        	  month="August";
	          }
	          else if(monthNumber.equals("09")){
	        	  month="September";
	          }
	          else if(monthNumber.equals("10")){
	        	  month="October";
	          }
	          else if(monthNumber.equals("11")){
	        	  month="November";
	          }
	          else if(monthNumber.equals("12")){
	        	  month="December";
	          }
	          return month;
	  	}
	  	
	  	//get activity's current year
	  	public String getCurrentYear() {
	          SimpleDateFormat dateFormat = new SimpleDateFormat(
	                  "yyyy", Locale.getDefault());
	          Date date = new Date();
	          return dateFormat.format(date);
	  	}

	/*@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.searchMonitor:
			{
				 int monitor = this.getArguments().getInt("monitor");
				 Bundle bundle = new Bundle();
          		 bundle.putInt("isConnected", monitor);
				 Intent i = new Intent(getActivity(), SearchMonitor.class);
				 i.putExtras(bundle);
			     startActivity(i);
			}
		}
		
	}*/
}