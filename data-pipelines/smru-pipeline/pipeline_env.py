"""
This module loads environment variables from `.env` and `pipeline_env.yaml` to the imported namespace.
The yaml file may contain shell variables which must be specified in the `.env` file.
A yaml file is required, the .env file is optional.

Usage:
```yaml
# pipeline.yaml
database: &id_database
    host_user: $HOST_USER
    port: $PORT
service:
    connection *id_database # reuses the `database` key
    name: frontend
    log_file: /path/to/file.log
```
```sh
# .env
HOST_USER=john
PORT=8080
```
See usage examples in the decorator `load_env` and the function `load_latest`

"""

from collections import namedtuple
import functools
import contextlib
import os
from pathlib import Path
from envyaml import EnvYAML

cwd = Path(__file__).parent
cwd = Path("../../config/smru-pipeline/")

def load_env(name, *, yaml_file=cwd/"pipeline_env.yaml", env_file=cwd/".env"):
    """Decorator that will load/inject the environment variable (from pipeline_env.yaml and .env), 
    and restore the os.environ when decorator is complete.  
    The subsequent the `kwargs` is updated with entries for the desired `name` which is defined
    with a combination of `yaml_file` and `env_file`.

    Args:
        name (str): key to load from environment
        yaml_file (str, mandatory): path to yaml formatted file with environment variables. Defaults to "env.yaml".
        env_file (str, optional): path to yaml formatted file with environment variables. Defaults to ".env".        

    Usage:

    ```python 
    # main.py
    from pipeline_env import load_env

    @load_env('database')
    def myfunction(..., *kwargs):
        env = kwargs.pop('env')
        print(env.database) # {'host_user':'john', 'port':8080}
        ...
    ```

    ```python
    @load_env('database')
    @load_env('service.log_file', yaml_file='pipeline.yaml', env_file="docker.env")
    def myfunction(..., **kwargs):
        env = kwargs.pop('env')
        print(env.database) 
        # {'host_user':'john', 'port':8080}
        print(env) 
        # {'database':{'host_user':'john', 'port':8080}, 'log_file': '/path/to/file.log'}

        ...
    ```
    """

    def load(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            cwd = Path(__file__).parent

            with load_latest(name, yaml_file, env_file) as env:
                new_keys = tuple(env.keys())
                Env = namedtuple("Env", new_keys)

                if kwargs.get('env'):
                    left = kwargs.get('env')
                    # merge
                    Env = namedtuple("Env", left._fields + new_keys)
                    kwargs.update({'env': Env(*left, **env)})
                else:
                    right = ({'env': Env(**env)})
                    kwargs.update(right)

                return func(*args, **kwargs)
        return wrapper
    return load


@contextlib.contextmanager
def load_latest(name: str, yaml_file = cwd/"pipeline_env.yaml", env_file = cwd/".env"):
    """Load ``os.environ`` composed of `yaml_file` and `env_file` dictionary in-place.
    Restores `os.environ` when exiting the scope.
    https://stackoverflow.com/a/34333710

    Args:
        name (str): environment variable to load
        yaml_file (str, optional): path to `pipeline_env.yaml`. Defaults to 'env.yaml'.
        env_file (str, optional): path to `.env`. Defaults to ".env".

    Yields:
        EnvYAML: environment variables loaded with `EnvYAML`

    Usage:

    ```python
    > with load_latest("smru_random_events.log_file") as ctx: # assuming `env.yaml` and `.env`
    >    print(ctx)
    >
    {'log_file': '/path/to/file.log'}

    > with load_latest("smru_random_events.log_file",  yaml_file='pipeline.yaml', env_file="docker.env") as ctx:
    >    print(ctx)
    >
    {'log_file': '/path/to/file.log'}
    ```
    """

    _env = os.environ
    env = EnvYAML(yaml_file, env_file)

    try:
        param = env.get(name)
        if isinstance(param, dict):
            yield param
        else:
            key = name.rsplit(".", 1)[-1]
            yield {key: param}
    finally:
        os.environ.clear()
        os.environ.update(_env)
