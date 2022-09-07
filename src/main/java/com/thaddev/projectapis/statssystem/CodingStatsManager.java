package com.thaddev.projectapis.statssystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thaddev.projectapis.ProjectApisApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CodingStatsManager {
    private CodingStats response;

    private static String url = "https://api.github.com/graphql";
    /*private static String query = "{" +
            "user(login: \"MyNameTsThad\") {" +
            "followers {" +
            "totalCount" +
            "                }" +
            "                repositories(last: 100) {" +
            "                    totalCount" +
            "                    nodes {" +
            "                        stargazerCount" +
            "                    }" +
            "                }" +
            "                contributionsCollection {" +
            "                    totalCommitContributions" +
            "                    totalPullRequestContributions" +
            "                    totalRepositoriesWithContributedCommits" +
            "                }" +
            "            }" +
            "        }";*/
    private static String query = "{\"query\":\"{user(login: \\\"MyNameTsThad\\\") {followers {totalCount}repositories(last: 100) {totalCount,nodes {stargazerCount}}contributionsCollection {totalCommitContributions,totalPullRequestContributions,totalRepositoriesWithContributedCommits}}}\"}";

    public CodingStatsManager() {
        ProjectApisApplication.instance.setCodingStatsManager(this);
        response = new CodingStats();
        ProjectApisApplication.instance.logger.info("Starting CodingStats Fetch Thread...");

        new Thread(() -> {
            fetchGithubStatsData();
            new Timer("CodingStatsFetcher").scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    fetchGithubStatsData();
                }
            }, 3600000, 3600000);
        }, "CodingStatsFetcher").start();
    }

    private void fetchGithubStatsData() {
        ProjectApisApplication.instance.logger.info("Fetching...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HttpRequest requestGithub = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/graphql"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + ProjectApisApplication.githubAuth)
                    .method("POST", HttpRequest.BodyPublishers.ofString(query))
                    .build();
            HttpRequest requestLeetcode = HttpRequest.newBuilder()
                    .uri(URI.create("https://leetcode.com/graphql/"))
                    .header("cookie", "csrftoken=ov45L9pMa5tARtU8yI17ugnIrHHqlJx5ZC1ItCNHAxWE12rd4JzJCC4qdxtPWwPP")
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.ofString("{\"query\":\"    query userProblemsSolved($username: String!) {  \\n        matchedUser(username: $username) {\\n            submitStatsGlobal {      \\n                acSubmissionNum {           \\n                    count\\n                }\\n            }\\n\\t\\t\\t\\t\\t\\tprofile {      \\n\\t\\t\\t\\t\\t\\t\\t\\tranking  \\n\\t\\t\\t\\t\\t\\t}\\n        }\\n\\t\\t\\t\\tuserContestRanking(username: $username) {   \\n\\t\\t\\t\\t\\t\\t\\tattendedContestsCount    \\n\\t\\t\\t\\t\\t\\t\\trating    \\n\\t\\t\\t\\t\\t\\t\\tglobalRanking\\n\\t\\t\\t\\t\\t\\t\\ttopPercentage\\n\\t\\t\\t\\t}  \\n    }\",\"variables\":{\"username\":\"ThadDev\"},\"operationName\":\"userProblemsSolved\"}"))
                    .build();
            HttpResponse<String> originalResponseGithub = HttpClient.newHttpClient().send(requestGithub, HttpResponse.BodyHandlers.ofString());
            String githubResponseString = originalResponseGithub.body().replaceFirst("\"data\"", "\"githubData\"");
            HttpResponse<String> originalResponseLeetcode = HttpClient.newHttpClient().send(requestLeetcode, HttpResponse.BodyHandlers.ofString());
            String leetcodeResponseString = originalResponseLeetcode.body().replaceFirst("\"data\"", "\"leetcodeData\"");
            User parsedGithubUserData = objectMapper.readValue(githubResponseString, GithubResponse.class).githubData.user;
            LeetcodeData parsedLeetcodeUserData = objectMapper.readValue(leetcodeResponseString, LeetcodeResponse.class).leetcodeData;

            //github
            response.getGithubStats().setFollowers(parsedGithubUserData.followers.totalCount);
            response.getGithubStats().setProjects(parsedGithubUserData.repositories.totalCount);
            response.getGithubStats().setCommits(parsedGithubUserData.contributionsCollection.totalCommitContributions);
            response.getGithubStats().setPulls(parsedGithubUserData.contributionsCollection.totalPullRequestContributions);
            response.getGithubStats().setContribs(parsedGithubUserData.contributionsCollection.totalRepositoriesWithContributedCommits);
            int stars = 0;
            for (Node node : parsedGithubUserData.repositories.nodes) {
                stars += node.stargazerCount;
            }
            response.getGithubStats().setStars(stars);

            //leetcode
            response.getLeetcodeStats().setProblems(parsedLeetcodeUserData.matchedUser.submitStatsGlobal.acSubmissionNum.get(0).count);
            response.getLeetcodeStats().setRank(parsedLeetcodeUserData.matchedUser.profile.ranking);
            response.getLeetcodeStats().setContests(parsedLeetcodeUserData.userContestRanking.attendedContestsCount);
            response.getLeetcodeStats().setTopContestRanking(parsedLeetcodeUserData.userContestRanking.topPercentage);
            response.getLeetcodeStats().setContestGlobalRanking(parsedLeetcodeUserData.userContestRanking.globalRanking);
            response.getLeetcodeStats().setContestRating((int) Math.round(parsedLeetcodeUserData.userContestRanking.rating));

            ProjectApisApplication.instance.logger.info("Fetch Job Finished.");
        } catch (IOException | InterruptedException e) {
            ProjectApisApplication.instance.logger.info("Fetch failed with message '" + e.getMessage() + "'.");
            e.printStackTrace();
        }
    }

    public void genId() {
        response.setId((short) new Random().nextInt(Short.MAX_VALUE));
    }

    public CodingStats getResponse() {
        genId();
        return response;
    }

    public void setHackerrankResponse(HackerrankStats stats) {
        response.setHackerrankStats(stats);
    }

    public void setHackerrankResponse(int badges, int skills, int questions) {
        response.setHackerrankStats(new HackerrankStats(badges, skills, questions));
    }
}
