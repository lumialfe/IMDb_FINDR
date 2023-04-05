package org.imdb.util;

import org.imdb.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImbdReader {

    private BufferedReader basics;
    private BufferedReader akas;
    private BufferedReader ratings;
    private BufferedReader crew;
    private BufferedReader participants;

    private String basicsLine;
    private String akasLine;
    private String ratingsLine;
    private String crewLine;
    private String participantLine;

    public boolean lines = true;
    public boolean rtng = true;
    public boolean dir = true;
    public boolean act = true;
    public boolean aka = true;



    public ImbdReader(MultipartFile basics, MultipartFile akas,
                      MultipartFile ratings, MultipartFile crew,
                      MultipartFile participants) throws IOException {
        this.basics = getBufferedReader(basics);
        this.akas = getBufferedReader(akas);
        this.ratings = getBufferedReader(ratings);
        this.crew = getBufferedReader(crew);
        this.participants = getBufferedReader(participants);
        getHeaders();

    }

    public BufferedReader getBufferedReader(MultipartFile file){
        try {
            BufferedReader buffer =
                    new BufferedReader(new InputStreamReader(file.getInputStream()));
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getHeaders() throws IOException {
        String headers =
                basics.readLine() + akas.readLine() + ratings.readLine() +
                 crew.readLine() + participants.readLine();
    }

    public void getLines() throws IOException {
        basicsLine = basics.readLine();
        if(aka) {
            akasLine = akas.readLine();
        }
        if(rtng) {
            ratingsLine = ratings.readLine();
        }
        if(dir) {
            crewLine = crew.readLine();
        }
        if(act) {
            participantLine = participants.readLine();
        }
    }


    public Movie getMovie() throws IOException {
        if(noLines()){
            lines = false;
            return null;
        }
        String[] parts = basicsLine.split("\t");
        String titleId = check(parts[0]);
        String titleType = check(parts[1]);
        String primaryTitle = check(parts[2]);
        String originalTitle = check(parts[3]);
        int isAdult = Integer.parseInt(check(parts[4]));
        if(isAdult == 1){
            return null;
        }
        int startYear = Integer.parseInt(check(parts[5]));
        int endYear = Integer.parseInt(check(parts[6]));
        int runtimeMinutes = Integer.parseInt(check(parts[7]));
        String genres[]= parts[8].split(",");

        //ratings file
        //set ratings
        String[] rtngs = doRatings(titleId);
        double avgRating = Double.parseDouble(check(rtngs[1]));
        int numVotes = Integer.parseInt(check(rtngs[2]));

        //get akas
        List<Akas> akas = doAkas(titleId);
        
        //get directors
        List<Directors> directors = doDirectors(titleId);

        //getActors
        List<Starring> starrings = doActors(titleId);


        return createMovie(titleId, titleType, primaryTitle, originalTitle,
                false,
                startYear, endYear, runtimeMinutes, genres, avgRating,
                numVotes, akas,
                directors, starrings);
    }

    private Movie createMovie(String titleId, String titleType, String
            primaryTitle, String originalTitle, boolean isAdult, int startYear,
                              int endYear, int runtimeMinutes, String[] genres,
                              double avgRating, int numVotes, List<Akas> akas,
                              List<Directors> directors, List<Starring> starrings) {
        Movie movie = new Movie();
        movie.setTconst(titleId);
        movie.setTitleType(titleType);
        movie.setPrimaryTitle(primaryTitle);
        movie.setOriginalTitle(originalTitle);
        movie.setAdult(isAdult);
        movie.setStartYear(startYear);
        movie.setEndYear(endYear);
        movie.setRuntimeMinutes(runtimeMinutes);
        movie.setGenres(genres);
        movie.setAvgRating(avgRating);
        movie.setNumVotes(numVotes);
        movie.setAkas(akas);
        movie.setDirectors(directors);
        movie.setStarrings(starrings);
        return movie;

    }

    private boolean noLines() {
        return basicsLine == null;
    }

    private List<Starring> doActors(String titleId) throws IOException {
        List<Starring> starrings = new ArrayList<>();

        if(lineIsNull(participantLine)){
            return starrings;
        }

        String[] parts = participantLine.split("\t");

        while(!parts[0].equals(titleId)) {
            if(isSmaller(titleId, parts[0])){
                act=false;
                return starrings;
            }else{
                participantLine = participants.readLine();
                if(lineIsNull(participantLine)){
                    return starrings;
                }
                parts = participantLine.split("\t");
            }
        }

        while(parts[0].equals(titleId)){
            //String[] parts = participantLine.split("\t");
            Name name = new Name();
            name.setNconst(parts[2]);
            Starring starring = new Starring();
            starring.setName(name);
            starring.setCharacters(parts[5]);
            starrings.add(starring);

            participantLine = participants.readLine();
            parts = participantLine.split("\t");
        }
        act = true;
        return starrings;
    }

    private List<Directors> doDirectors(String titleId) throws IOException {
        List<Directors> directors = new ArrayList<>();

        if(lineIsNull(crewLine)){
            return directors;
        }

        String[] parts = crewLine.split("\t");
        while(!parts[0].equals(titleId)) {
            if(isSmaller(titleId, parts[0])){
                dir=false;
                return directors;
            }else{
                crewLine = crew.readLine();
                if(lineIsNull(crewLine)){
                    return directors;
                }
                parts = crewLine.split("\t");
            }
        }

        if (parts[1].contains(",")) {
            String[] dir = parts[1].split(",");
            addDirectors(directors, dir);

        } else {
            Directors d = new Directors();
            d.setNconst(parts[1]);
            directors.add(d);
        }
        dir = true;
        return directors;
    }

    private void addDirectors(List<Directors> directors, String[] dir) {
        for(int i = 0; i < dir.length; i++){
            Directors d = new Directors();
            d.setNconst(dir[i]);
            directors.add(d);
        }
    }

    private List<Akas> doAkas(String titleId) throws IOException {
        List<Akas> akasList = new ArrayList<>();

        if(lineIsNull(akasLine)){
            return akasList;
        }

        String[] parts = akasLine.split("\t");

        while(!parts[0].equals(titleId)) {
            if(isSmaller(titleId, parts[0])){
                aka=false;
                return akasList;
            }else{
                akasLine = akas.readLine();
                if(lineIsNull(akasLine)){
                    return akasList;
                }
                parts = akasLine.split("\t");
            }
        }
        while(parts[0].equals(titleId)){
            boolean isOriginal = true;
            if(parts[7].equals("0")){
                isOriginal = false;
            }
            Akas aka = new Akas();
            aka.setTitle(parts[2]);
            aka.setRegion(parts[3]);
            aka.setLanguage(parts[4]);
            aka.setOriginal(isOriginal);
            akasList.add(aka);

            akasLine = akas.readLine();
            parts = akasLine.split("\t");
        }
        aka=true;
        return akasList;
    }

    private String[] doRatings(String titleId) throws IOException {
        if (lineIsNull(ratingsLine)) {
            return new String[]{"id", "0.0", "0"};
        }
        String[] parts = ratingsLine.split("\t");
        while(!parts[0].equals(titleId)){
            if(isSmaller(titleId, parts[0])){
                rtng = false;
                return new String[]{titleId, "0.0", "0"};
            }else{
                ratingsLine = ratings.readLine();
                if(lineIsNull(ratingsLine)) {
                    return new String[]{titleId, "0.0", "0"};
                }
                parts = ratingsLine.split("\t");
            }
        }
        rtng = true;
        return parts;
    }


    private boolean isSmaller(String titleId, String part) {
        int id = Integer.parseInt(part.split("tt")[1]);
        int title = Integer.parseInt(titleId.split("tt")[1]);
        if(title < id){
            return true;
        }
        return false;
    }

    private String check(String part) {
        if(part.equals("\\N")){
            return "0";
        }
        return part;
    }

    private boolean lineIsNull(String line){
        return line == null;
    }
}
