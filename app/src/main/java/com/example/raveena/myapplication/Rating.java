package com.example.raveena.myapplication;


public class Rating {
    public String username, comments, collname;
    public float infra, placement, crowd, faculty, orating;

    Rating(){

    }

    Rating(String collname, String username, String comments, float infra, float placement, float crowd, float faculty){

        this.collname = collname;
        this.username = username;
        this.comments = comments;
        this.infra = infra;
        this.placement = placement;
        this.crowd = crowd;
        this.faculty = faculty;
        orating = (infra + placement + crowd + faculty)/4 ;
    }

    public String getComments(){
        return comments;
    }
    public String getUsername(){
        return username;
    }
    public String getCollname() { return collname; }

    public float getInfra(){
        return infra;
    }
    public float getPlacement(){
        return placement;
    }
    public float getCrowd(){
        return crowd;
    }
    public float getFaculty(){
        return faculty;
    }
    public float getOrating(){
        return orating;
    }

    public void setComments(String comments){
        this.comments = comments;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setCollname(String collname) { this.collname = collname; }

    public void setInfra(float infra){
        this.infra = infra;
    }
    public void setPlacement(float placement){
        this.placement = placement;
    }
    public void setCrowd(float crowd){
        this.crowd = crowd;
    }
    public void setFaculty(float faculty){
        this.faculty = faculty;
    }
    public void setOrating(float orating){
        this.orating = orating;
    }


}
