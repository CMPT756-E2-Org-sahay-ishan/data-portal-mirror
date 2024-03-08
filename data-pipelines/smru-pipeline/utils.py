# WIP: not ready for use

from pipeline_env import load_latest
from pathlib import Path
import yaml
import sys
# from __main__ import LOG_CONFIG

seconds_2_milliseconds = lambda x : int(round( x.timestamp() * 1000 ))

try:
    from loguru_config import LoguruConfig
    from loguru import logger
    MODULE_FOUND = True
except ModuleNotFoundError as err:
    MODULE_FOUND = False
    print("Module not found error. Likely loguru or loguru-config are not installed in this environment.", err)

def configure_logger(name=__file__, level="TRACE", **kwargs):
    if not MODULE_FOUND:
        return False
    
    with load_latest("project.log_config") as ctx:
        LOG_CONFIG = ctx.get('log_config')

    # LoguruConfig.load(config_file)
    
    logger.remove() # clear the pre-configured logger
    logger.add(sys.stderr, format=LOG_CONFIG.get("format"), diagnose= LOG_CONFIG.get("diagnose")) # recreate with custom format

    key = "log_file"
    name = name.rsplit(".py", maxsplit=1)[0].strip()
    # log_file="test2.log".removesuffix(".py").rstrip()

    lookup = ".".join([name, key])  # main2.log_file

    log_file = ''
    with load_latest(lookup, kwargs.get('yaml_file'), kwargs.get('env_file')) as ctx:
        log_file = ctx.get(key)

    print(f"{LOG_CONFIG=}")
    print(f"{log_file=}")
    logger.add(sink=log_file, level=level, **LOG_CONFIG)
    
    return True


def get_log_dir(log_dir="logs/.", name=""):
    log_dir += "/."
    target = (Path(log_dir) / name).resolve()
    print(f"is_dir: {target.is_dir()}: {target=}")

    if target.is_dir():
        if target.cwd() == target:
            print("same")
            target = target / target.stem  # repeat folder name
        print('folder')
        log_dir = str(target)+"/"

    elif target.is_file():
        print("file")

        target = target.resolve().with_suffix("")  # remove extension
        log_dir = str(target)

    print(f"is_dir: {target.is_dir()}: {log_dir=}")
    return log_dir

