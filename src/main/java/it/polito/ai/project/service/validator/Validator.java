package it.polito.ai.project.service.validator;

import it.polito.ai.project.service.model.Haversine;
import it.polito.ai.project.service.model.TimedPosition;

public class Validator {

    private static final double lat_inf = -90.00;
    private static final double lat_sup = +90.00;
    private static final double long_inf = -180.00;
    private static final double long_sup = +180.00;
    private static final double max_v = 100.00;

    public Validator() {
    }

    /**
     * This method validates the first coordinate of the sequence (only data validity)
     * @param t the first coordinate of the sequence
     * @return true if the first coordinate is valid, false otherwise
     */
    public boolean validateFirst(TimedPosition t){
        return validateCoordinate(t);
    }

    /**
     * this method validates two coordinates in sequence (data validity + sequence validity)
     * @param t1 the first coordinate
     * @param t2 the second coordinate
     * @return true if the sequence is valid, false otherwise
     */
    public boolean validateSequence(TimedPosition t1, TimedPosition t2){

        // check validity of t2 (assume t1 is correct)
        if(!validateCoordinate(t2)){
            return false;
        }
        // check t2 > t1
        if(t2.getTimestamp() <= t1.getTimestamp()){
            return false;
        }
        // check distance/time < 100 ms
        double distance = Haversine.distance(t1.retriveLat(), t1.retrieveLng(), t2.retriveLat(), t2.retrieveLng());
        long diffTime = t2.getTimestamp() - t1.getTimestamp();
        if(diffTime == 0){
            return false;
        }
        double time = diffTime/1000;
        return !(distance / time >= max_v);
    }

    /**
     * This method validates the coordinate (only data validity)
     * @param t the coordinate
     * @return true if the coordinate is valid, false otherwise
     */
    private boolean validateCoordinate(TimedPosition t){

        //check not null and timestamp >=0
        if(t == null || t.getTimestamp() < 0){
            return false;
        }
        // check -90.00 <= lat <= +90.00  and -180.00 <= long <= +180.00
        if(t.retriveLat()> lat_sup || t.retriveLat() < lat_inf){
            return false;
        }
        return !(t.retrieveLng() > long_sup) && !(t.retrieveLng() < long_inf);
    }


}
