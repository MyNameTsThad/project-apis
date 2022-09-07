package com.thaddev.projectapis.statssystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class CodingStats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private GithubStats githubStats;
    @OneToOne
    private HackerrankStats hackerrankStats;
    @OneToOne
    private LeetcodeStats leetcodeStats;

    public CodingStats() {
        this.githubStats = new GithubStats();
        this.hackerrankStats = new HackerrankStats();
        this.leetcodeStats = new LeetcodeStats();
    }

    public void setId(int id) {
        this.id = id;
    }

    public CodingStats(GithubStats githubStats, HackerrankStats hackerrankStats, LeetcodeStats leetcodeStats) {
        this.githubStats = githubStats;
        this.hackerrankStats = hackerrankStats;
        this.leetcodeStats = leetcodeStats;
    }

    @Override
    public String toString() {
        return "CodingStats{" +
                "id=" + id +
                ", githubStats=" + githubStats +
                ", hackerrankStats=" + hackerrankStats +
                ", leetcodeStats=" + leetcodeStats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodingStats that = (CodingStats) o;
        return id == that.id && Objects.equals(githubStats, that.githubStats) && Objects.equals(hackerrankStats, that.hackerrankStats) && Objects.equals(leetcodeStats, that.leetcodeStats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, githubStats, hackerrankStats, leetcodeStats);
    }

    public int getId() {
        return id;
    }

    public GithubStats getGithubStats() {
        return githubStats;
    }

    public void setGithubStats(GithubStats githubStats) {
        this.githubStats = githubStats;
    }

    public HackerrankStats getHackerrankStats() {
        return hackerrankStats;
    }

    public void setHackerrankStats(HackerrankStats hackerrankStats) {
        this.hackerrankStats = hackerrankStats;
    }

    public LeetcodeStats getLeetcodeStats() {
        return leetcodeStats;
    }

    public void setLeetcodeStats(LeetcodeStats leetcodeStats) {
        this.leetcodeStats = leetcodeStats;
    }
}

@Entity
class GithubStats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int followers = 0;
    private int projects = 0;
    private int stars = 0;
    private int commits = 0;
    private int pulls = 0;
    private int contribs = 0;

    public GithubStats() {
    }

    public GithubStats(int followers, int projects, int stars, int commits, int pulls, int contribs) {
        this.followers = followers;
        this.projects = projects;
        this.stars = stars;
        this.commits = commits;
        this.pulls = pulls;
        this.contribs = contribs;
    }

    @Override
    public String toString() {
        return "GithubStats{" +
                "id=" + id +
                ", followers=" + followers +
                ", projects=" + projects +
                ", stars=" + stars +
                ", commits=" + commits +
                ", pulls=" + pulls +
                ", contribs=" + contribs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubStats that = (GithubStats) o;
        return id == that.id && followers == that.followers && projects == that.projects && stars == that.stars && commits == that.commits && pulls == that.pulls && contribs == that.contribs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, followers, projects, stars, commits, pulls, contribs);
    }

    public int getId() {
        return id;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getProjects() {
        return projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public int getPulls() {
        return pulls;
    }

    public void setPulls(int pulls) {
        this.pulls = pulls;
    }

    public int getContribs() {
        return contribs;
    }

    public void setContribs(int contribs) {
        this.contribs = contribs;
    }
}

@Entity
class HackerrankStats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int badges = 0;
    private int skills = 0;
    private int questions = 0;

    public HackerrankStats() {
    }

    public HackerrankStats(int badges, int skills, int questions) {
        this.badges = badges;
        this.skills = skills;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "HackerrankStats{" +
                "id=" + id +
                ", badges=" + badges +
                ", skills=" + skills +
                ", questions=" + questions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HackerrankStats that = (HackerrankStats) o;
        return id == that.id && badges == that.badges && skills == that.skills && questions == that.questions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, badges, skills, questions);
    }

    public int getId() {
        return id;
    }

    public int getBadges() {
        return badges;
    }

    public void setBadges(int badges) {
        this.badges = badges;
    }

    public int getSkills() {
        return skills;
    }

    public void setSkills(int skills) {
        this.skills = skills;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }
}

@Entity
class LeetcodeStats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int rank = 0;
    private int contests = 0;
    private int contestRating = 0;
    private int contestGlobalRanking = 0;
    private int problems = 0;
    private double topContestRanking = 0;

    public LeetcodeStats() {
    }

    public LeetcodeStats(int rank, int contests, int contestRating, int contestGlobalRanking, int problems, float topContestRanking) {
        this.rank = rank;
        this.contests = contests;
        this.contestRating = contestRating;
        this.contestGlobalRanking = contestGlobalRanking;
        this.problems = problems;
        this.topContestRanking = topContestRanking;
    }

    @Override
    public String toString() {
        return "LeetcodeStats{" +
                "id=" + id +
                ", rank=" + rank +
                ", contests=" + contests +
                ", contestRating=" + contestRating +
                ", contestGlobalRanking=" + contestGlobalRanking +
                ", problems=" + problems +
                ", topContestRanking=" + topContestRanking +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeetcodeStats that = (LeetcodeStats) o;
        return id == that.id && rank == that.rank && contests == that.contests && contestRating == that.contestRating && contestGlobalRanking == that.contestGlobalRanking && problems == that.problems && Double.compare(that.topContestRanking, topContestRanking) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rank, contests, contestRating, contestGlobalRanking, problems, topContestRanking);
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getContests() {
        return contests;
    }

    public void setContests(int contests) {
        this.contests = contests;
    }

    public int getContestRating() {
        return contestRating;
    }

    public void setContestRating(int contestRating) {
        this.contestRating = contestRating;
    }

    public int getContestGlobalRanking() {
        return contestGlobalRanking;
    }

    public void setContestGlobalRanking(int contestGlobalRanking) {
        this.contestGlobalRanking = contestGlobalRanking;
    }

    public int getProblems() {
        return problems;
    }

    public void setProblems(int problems) {
        this.problems = problems;
    }

    public double getTopContestRanking() {
        return topContestRanking;
    }

    public void setTopContestRanking(double topContestRanking) {
        this.topContestRanking = topContestRanking;
    }
}
