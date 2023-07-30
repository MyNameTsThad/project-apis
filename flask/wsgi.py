import time

from app import app, Interval, fetchStats
import os

if __name__ == "__main__":
    # Create an interval.
    interval = Interval(3600, fetchStats, args=[time.time(), ])
    print("Starting Interval...")
    interval.start()
    app.run(host='0.0.0.0', port=os.environ.get("FLASK_SERVER_PORT"), debug=False)
    interval.stop()

