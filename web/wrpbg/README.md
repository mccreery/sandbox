# World Record/Personal Best Grabber
This server app looks up WRs and PBs from
[Speedrun.com](https://www.speedrun.com/). It serves a JSON endpoint at `/data`
and a homepage displaying the WR and your PB. You will have to fill in the game
ID and your ID from the [Speedrun.com
API](https://github.com/speedruncomorg/api).

## Run
```
python -m venv venv/
. venv/Scripts/activate
pip install -r requirements.txt
python ./pb_server.py
```
Then navigate to `localhost`
