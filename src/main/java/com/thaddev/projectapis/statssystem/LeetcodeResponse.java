package com.thaddev.projectapis.statssystem;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
class AcSubmissionNum {
    public int count;
}

class LeetcodeData {
    public MatchedUser matchedUser;
    public UserContestRanking userContestRanking;
}

class MatchedUser {
    public SubmitStatsGlobal submitStatsGlobal;
    public Profile profile;
}

class Profile {
    public int ranking;
}

public class LeetcodeResponse {
    public LeetcodeData leetcodeData;
}

class SubmitStatsGlobal {
    public ArrayList<AcSubmissionNum> acSubmissionNum;
}

class UserContestRanking {
    public int attendedContestsCount;
    public double rating;
    public int globalRanking;
    public double topPercentage;
}

