import json
import time
import requests
import os

from flask import Flask, request
from threading import Timer
from functools import partial

app = Flask(__name__)

PAPI_GITHUB_AUTH = os.environ.get('PAPI_GITHUB_AUTH')

githubUrl = "https://api.github.com/graphql"
leetCodeUrl = "https://leetcode.com/graphql/"

githubPayload = \
    "{\"query\":\"{user(login: \\\"MyNameTsThad\\\") {followers {totalCount}repositories(last: 100) {totalCount" \
    ",nodes {stargazerCount}}contributionsCollection {totalCommitContributions,totalPullRequestContributions," \
    "totalRepositoriesWithContributedCommits}}}\"}"
githubHeaders = {
    "Content-Type": "application/json",
    "Authorization": "Bearer " + PAPI_GITHUB_AUTH
}

leetCodePayload = \
    "{\"query\":\"query userProblemsSolved($username: String!) {matchedUser(username: $username) {submitStatsGlobal" \
    " {acSubmissionNum {count}}profile {ranking}}userContestRanking(username: $username) {attendedContestsCount," \
    "rating,globalRanking,topPercentage}}\",\"operationName\":\"userProblemsSolved\",\"variables\":{\"username\":" \
    "\"ThadDev\",\"operationName\":\"userProblemsSolved\"}}"
leetCodeHeaders = {
    "Content-Type": "application/json"
}


class GithubStats:
    def __init__(self, followers, projects, stars, commits, pulls, contribs):
        self.followers = followers
        self.projects = projects
        self.stars = stars
        self.commits = commits
        self.pulls = pulls
        self.contribs = contribs


class HackerRankStats:
    def __init__(self, badges, skills, questions):
        self.badges = badges
        self.skills = skills
        self.questions = questions


class LeetcodeStats:
    def __init__(self, rank, contests, contestRating, contestGlobalRanking, problems, topContestRanking):
        self.rank = rank
        self.contests = contests
        self.contestRating = contestRating
        self.contestGlobalRanking = contestGlobalRanking
        self.problems = problems
        self.topContestRanking = topContestRanking


class CodingStats:
    def __init__(self, githubStats, hackerRankStats, leetcodeStats):
        self.githubStats = githubStats
        self.hackerRankStats = hackerRankStats
        self.leetcodeStats = leetcodeStats

    def toDict(self):
        return {
            'githubStats': self.githubStats.__dict__,
            'hackerRankStats': self.hackerRankStats.__dict__,
            'leetcodeStats': self.leetcodeStats.__dict__
        }


# https://gist.github.com/bbengfort/a7d46013f39cf367daa5
class Interval(object):

    def __init__(self, interval, function, args=[], kwargs={}):
        """
        Runs the function at a specified interval with given arguments.
        """
        self.interval = interval
        self.function = partial(function, *args, **kwargs)
        self.running = False
        self._timer = None

    def __call__(self):
        """
        Handler function for calling the partial and continuting.
        """
        self.running = False  # mark not running
        self.start()  # reset the timer for the next go
        self.function()  # call the partial function

    def start(self):
        """
        Starts the interval and lets it run.
        """
        if self.running:
            # Don't start if we're running!
            return

            # Create the timer object, start and set state.
        self.function()  # execute now
        self._timer = Timer(self.interval, self)
        self._timer.start()
        self.running = True

    def stop(self):
        """
        Cancel the interval (no more function calls).
        """
        if self._timer:
            self._timer.cancel()
        self.running = False
        self._timer = None


response = CodingStats(
    GithubStats(0, 0, 0, 0, 0, 0),
    HackerRankStats(3, 5, 225),
    LeetcodeStats(0, 0, 0, 0, 0, 0)
)


# fetch job for response
def fetchStats(start):
    print("Fetching stats...")
    timeStart = time.time()
    githubResponse = requests.request("POST", githubUrl, data=githubPayload, headers=githubHeaders).json()['data']
    leetCodeResponse = requests.request("POST", leetCodeUrl, data=leetCodePayload, headers=leetCodeHeaders).json()['data']
    response.githubStats.followers = githubResponse['user']['followers']['totalCount']
    response.githubStats.projects = githubResponse['user']['repositories']['totalCount']
    response.githubStats.stars = sum([project['stargazerCount'] for project in githubResponse['user']['repositories']['nodes']])
    response.githubStats.commits = githubResponse['user']['contributionsCollection']['totalCommitContributions']
    response.githubStats.pulls = githubResponse['user']['contributionsCollection']['totalPullRequestContributions']
    response.githubStats.contribs = githubResponse['user']['contributionsCollection']['totalRepositoriesWithContributedCommits']
    response.leetcodeStats.rank = leetCodeResponse['matchedUser']['profile']['ranking']
    response.leetcodeStats.contests = leetCodeResponse['userContestRanking']['attendedContestsCount']
    response.leetcodeStats.contestRating = leetCodeResponse['userContestRanking']['rating']
    response.leetcodeStats.contestGlobalRanking = leetCodeResponse['userContestRanking']['globalRanking']
    response.leetcodeStats.problems = sum([submission['count'] for submission in leetCodeResponse['matchedUser']['submitStatsGlobal']['acSubmissionNum']])
    response.leetcodeStats.topContestRanking = leetCodeResponse['userContestRanking']['topPercentage']
    print("Fetched stats in " + str(time.time() - timeStart) + " seconds.")


@app.route('/api-v1/codingstats/get', methods=['GET'])
def getCodingStats():
    return json.dumps(response.toDict())


@app.route("/api-v1/codingstats/sethr", methods=['POST'])
def setHackerRankStats():
    data = request.json
    response.hackerRankStats.badges = data['badges']
    response.hackerRankStats.skills = data['skills']
    response.hackerRankStats.questions = data['questions']


if __name__ == '__main__':
    # Create an interval.
    interval = Interval(3600, fetchStats, args=[time.time(), ])
    print("Starting Interval, press CTRL+C to stop.")
    interval.start()
    app.run()
    interval.stop()
