package sample.Utilities;

import org.apache.commons.lang3.ArrayUtils;

public class FormatTime {


    public static  String formatTimeWithWords(long duration) {


        long years=(long)(duration/ 31536000);
        long partialYears=duration%31536000;
        long days=partialYears/86400;
        long partialDays=partialYears%86400;
        long hours=partialDays/3600;
        long partialHours=partialDays%3600;
        long minutes=partialHours/60;
        long partialMinutes=partialHours%60;
        long seconds=partialMinutes%60;
        

        return (" Total Time: Years: "+years+" Days: "+days+" Hours: "+hours+" Minutes: "+minutes+" Seconds: "+seconds);






    }

    public  static String formatTrackIndex(long durationInFrames) {



        long minutes=durationInFrames/(60*75);
        long partialMinutes=durationInFrames%(60*75);
        long seconds=partialMinutes/75;
        long frames=seconds%75;




        String minutesString=minutes+"";

        if(minutes<10){

            minutesString="0"+minutes;
        }


        String secondsString=seconds+"";

        if(seconds<10){

            secondsString="0"+seconds;
        }
        String totalTime="";

        totalTime=minutesString+":"+secondsString+":"+frames;






        return totalTime;






    }




    public  static String formatTimeWithColons(long duration) {


        long years=(long)(duration/ 31536000);
        long partialYears=duration%31536000;
        long days=partialYears/86400;
        long partialDays=partialYears%86400;
        long hours=partialDays/3600;
        long partialHours=partialDays%3600;
        long minutes=partialHours/60;
        long partialMinutes=partialHours%60;
        long seconds=partialMinutes%60;
        
       
        String daysString=days+"";

        if(partialDays<10){

            daysString="0"+days;
        }

        String hoursString=hours+"";

        if(hours<10){

            hoursString="0"+hours;
        }


        String minutesString=minutes+"";

        if(minutes<10){

            minutesString="0"+minutes;
        }


        String secondsString=seconds+"";

        if(seconds<10){

            secondsString="0"+seconds;
        }
        String totalTime="";
        if(years>0){
            totalTime=totalTime+years+":";
            
        }
        if(days>0){
            totalTime=totalTime+daysString+":";
            
        }
        if(hours>0){
            totalTime=totalTime+hoursString+":";
        }

            totalTime=totalTime+minutesString+":";



            totalTime=totalTime+secondsString;



        return totalTime;






    }

    public static  long getIndexFrames(String index ){
        String [] numbers=index.split(":");

        int minutes=0;
        int seconds=0;
        int frames=0;
        ArrayUtils.reverse(numbers);// reverse arry to make pasring easier

        if(numbers.length>2) {

            minutes=Integer.valueOf(numbers[2]);

        }

        if (numbers.length>1) {

            seconds=Integer.valueOf(numbers[1]);

        }

        if(numbers.length>0) {

            frames=Integer.valueOf(numbers[0]);



        }

        if(numbers.length<=0) {


            return 0;


        }
        return (minutes*60*75)+(seconds*75)+frames;





    }

    public static long unformatTimeWithColons(String time){
        String [] numbers=time.split(":");
        int years=0;
        int days=0;
        int hours=0;
        int minutes=0;
        int seconds=0;
        ArrayUtils.reverse(numbers);// reverse arry to make pasring easier

        if(numbers.length>4) {
             years = Integer.valueOf(numbers[4]);

        }
        if(numbers.length>3){
            days=Integer.valueOf(numbers[3]);



        }
        if(numbers.length>2) {

            hours=Integer.valueOf(numbers[2]);

        }

        if (numbers.length>1) {
            minutes=Integer.valueOf(numbers[1]);


        }

        if(numbers.length>0) {


            seconds=Integer.valueOf(numbers[0]);


        }

        if(numbers.length<=0) {


            return 0;


        }


        return (years*31536000)+(days*86400)+(hours*3600)+(minutes*60)+seconds;









    }



    public static int unformatIndexFramesToSeconds(String time){
        String [] numbers=time.split(":");

        int minutes=0;
        int seconds=0;
        int frames=0;
        ArrayUtils.reverse(numbers);// reverse arry to make pasring easier



        if (numbers.length>1) {
            minutes=Integer.valueOf(numbers[1]);


        }

        if(numbers.length>0) {


            seconds=Integer.valueOf(numbers[0]);


        }

        if(numbers.length<=0) {


            return 0;


        }


        return (minutes*60)+seconds;









    }




}