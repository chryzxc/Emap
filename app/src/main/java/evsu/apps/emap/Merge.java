package evsu.apps.emap;



public class Merge {
    private String newbuilding,newroom,newroomId,newfloor;

    public Merge(String newbuilding,String newroom, String newroomId, String newfloor)
    {
        this.newroomId = newroomId;
        this.newroom = newroom;
        this.newbuilding = newbuilding;
        this.newfloor = newfloor;
    }

    public  String getNewroomId(){
        return this.newroomId;
    }
    public  String getNewroom(){
        return this.newroom;
    }
    public String getNewbuilding(){
        return this.newbuilding;
    }
    public  String getNewfloor(){
        return this.newfloor;
    }
}
