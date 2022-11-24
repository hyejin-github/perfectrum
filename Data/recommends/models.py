from django.db import models

# Create your models here.
class Accords(models.Model):
    idx = models.AutoField(primary_key=True)
    accord_name = models.CharField(max_length=20)
    accord_description = models.CharField(max_length=100)
    accord_img = models.CharField(max_length=500)
    accord_class = models.ForeignKey('AccordsClasses', models.DO_NOTHING, db_column='accord_class')

    class Meta:
        managed = False
        db_table = 'accords'


class AccordsClasses(models.Model):
    idx = models.AutoField(primary_key=True)
    class_name = models.CharField(max_length=30)
    class_description = models.CharField(max_length=200)

    class Meta:
        managed = False
        db_table = 'accords_classes'

class HaveLists(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx', blank=True, null=True)
    is_delete = models.IntegerField()
    perfume_idx = models.ForeignKey('Perfumes', models.DO_NOTHING, db_column='perfume_idx')

    class Meta:
        managed = False
        db_table = 'have_lists'


class PerfumeAccords(models.Model):
    idx = models.AutoField(primary_key=True)
    perfume_idx = models.ForeignKey('Perfumes', models.DO_NOTHING, db_column='perfume_idx')
    accord_idx = models.ForeignKey(Accords, models.DO_NOTHING, db_column='accord_idx')

    class Meta:
        managed = False
        db_table = 'perfume_accords'


class Perfumes(models.Model):
    idx = models.AutoField(primary_key=True)
    brand_name = models.CharField(max_length=100)
    perfume_name = models.CharField(max_length=100)
    concentration = models.CharField(max_length=100)
    gender = models.CharField(max_length=100)
    scent = models.CharField(max_length=100)
    top_notes = models.CharField(max_length=300)
    middle_notes = models.CharField(max_length=300)
    base_notes = models.CharField(max_length=300)
    item_rating = models.FloatField()
    perfume_img = models.CharField(max_length=500)
    description = models.TextField()
    seasons = models.CharField(max_length=30)
    timezone = models.CharField(max_length=10)
    longevity = models.IntegerField()
    sillage = models.IntegerField()
    wish_count = models.IntegerField()
    have_count = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'perfumes'


class Reviews(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx')
    perfume_idx = models.ForeignKey(Perfumes, models.DO_NOTHING, db_column='perfume_idx')
    review_img = models.CharField(max_length=500, blank=True, null=True)
    total_score = models.IntegerField()
    longevity = models.IntegerField()
    sillage_score = models.IntegerField()
    content = models.CharField(max_length=500, blank=True, null=True)
    like_count = models.IntegerField()
    time = models.DateTimeField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'reviews'


class Surveys(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx')
    perfume_idx = models.ForeignKey(Perfumes, models.DO_NOTHING, db_column='perfume_idx')
    like_seasons = models.CharField(max_length=10)
    like_gender = models.CharField(max_length=10)
    like_longevity = models.IntegerField()
    like_timezone = models.CharField(max_length=10)
    like_accord_class = models.ForeignKey(AccordsClasses, models.DO_NOTHING, db_column='like_accord_class')

    class Meta:
        managed = False
        db_table = 'surveys'


class UserAccordClass(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx')
    accord_class_idx = models.ForeignKey(AccordsClasses, models.DO_NOTHING, db_column='accord_class_idx')

    class Meta:
        managed = False
        db_table = 'user_accord_class'


class UserAccords(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx')
    accord_idx = models.ForeignKey(Accords, models.DO_NOTHING, db_column='accord_idx')

    class Meta:
        managed = False
        db_table = 'user_accords'


class UserDetailLogs(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx')
    perfume_idx = models.ForeignKey(Perfumes, models.DO_NOTHING, db_column='perfume_idx')

    class Meta:
        managed = False
        db_table = 'user_detail_logs'


class UserSearchLogs(models.Model):
    idx = models.AutoField(primary_key=True)
    user_idx = models.ForeignKey('Users', models.DO_NOTHING, db_column='user_idx')
    gender = models.CharField(max_length=10)
    duration = models.IntegerField()
    accord_class = models.ForeignKey(UserAccordClass, models.DO_NOTHING, db_column='accord_class')

    class Meta:
        managed = False
        db_table = 'user_search_logs'


class Users(models.Model):
    user_id = models.CharField(max_length=30)
    profile_img = models.CharField(max_length=500, blank=True, null=True)
    nickname = models.CharField(max_length=20, blank=True, null=True)
    gender = models.CharField(max_length=10, blank=True, null=True)
    seasons = models.CharField(max_length=10, blank=True, null=True)
    idx = models.AutoField(primary_key=True)
    accord_class = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'users'


class WishLists(models.Model):
    idx = models.AutoField(primary_key=True)
    is_delete = models.IntegerField()
    user_idx = models.ForeignKey(Users, models.DO_NOTHING, db_column='user_idx')
    perfume_idx = models.ForeignKey(Perfumes, models.DO_NOTHING, db_column='perfume_idx')

    class Meta:
        managed = False
        db_table = 'wish_lists'