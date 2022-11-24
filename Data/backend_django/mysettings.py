DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME':'S07P22C105',
        'USER':'ssafy',
        'PASSWORD':'ssafy',    
        'HOST':'j7C105.p.ssafy.io', 
        'PORT':'3306'
    }
}

CACHES = {
    "default": {
        "BACKEND": "django_redis.cache.RedisCache",
        "LOCATION": "redis://j7c105.p.ssafy.io:6379",
        "OPTIONS": {
            "PASSWORD": "ssafyC105",
            "CLIENT_CLASS": "django_redis.client.DefaultClient",
        }
    }
}