package xyz.thaddev.projectapis.statssystem;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
class ContributionsCollection {
    public int totalCommitContributions;
    public int totalPullRequestContributions;
    public int totalRepositoriesWithContributedCommits;
}

class GithubData {
    public User user;
}

class Followers {
    public int totalCount;
}

class Node {
    public int stargazerCount;
}

class Repositories {
    public int totalCount;
    public ArrayList<Node> nodes;
}

public class GithubResponse {
    public GithubData githubData;
}

class User {
    public Followers followers;
    public Repositories repositories;
    public ContributionsCollection contributionsCollection;
}
