from django.urls import path
from . import views

urlpatterns = [
    path('recommend/', views.collaboration), # api/recommend
    path('recommend2/', views.collaboration2),
    path('recommend3/', views.collaboration3),
]

