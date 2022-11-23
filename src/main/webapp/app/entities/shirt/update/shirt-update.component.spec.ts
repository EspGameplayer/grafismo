import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ShirtService } from '../service/shirt.service';
import { IShirt, Shirt } from '../shirt.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';

import { ShirtUpdateComponent } from './shirt-update.component';

describe('Shirt Management Update Component', () => {
  let comp: ShirtUpdateComponent;
  let fixture: ComponentFixture<ShirtUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shirtService: ShirtService;
  let teamService: TeamService;
  let seasonService: SeasonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ShirtUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ShirtUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShirtUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shirtService = TestBed.inject(ShirtService);
    teamService = TestBed.inject(TeamService);
    seasonService = TestBed.inject(SeasonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Team query and add missing value', () => {
      const shirt: IShirt = { id: 456 };
      const team: ITeam = { id: 87137 };
      shirt.team = team;

      const teamCollection: ITeam[] = [{ id: 11391 }];
      jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
      const additionalTeams = [team];
      const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
      jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shirt });
      comp.ngOnInit();

      expect(teamService.query).toHaveBeenCalled();
      expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
      expect(comp.teamsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Season query and add missing value', () => {
      const shirt: IShirt = { id: 456 };
      const season: ISeason = { id: 16316 };
      shirt.season = season;

      const seasonCollection: ISeason[] = [{ id: 56285 }];
      jest.spyOn(seasonService, 'query').mockReturnValue(of(new HttpResponse({ body: seasonCollection })));
      const additionalSeasons = [season];
      const expectedCollection: ISeason[] = [...additionalSeasons, ...seasonCollection];
      jest.spyOn(seasonService, 'addSeasonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shirt });
      comp.ngOnInit();

      expect(seasonService.query).toHaveBeenCalled();
      expect(seasonService.addSeasonToCollectionIfMissing).toHaveBeenCalledWith(seasonCollection, ...additionalSeasons);
      expect(comp.seasonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const shirt: IShirt = { id: 456 };
      const team: ITeam = { id: 5550 };
      shirt.team = team;
      const season: ISeason = { id: 64793 };
      shirt.season = season;

      activatedRoute.data = of({ shirt });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(shirt));
      expect(comp.teamsSharedCollection).toContain(team);
      expect(comp.seasonsSharedCollection).toContain(season);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Shirt>>();
      const shirt = { id: 123 };
      jest.spyOn(shirtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shirt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shirt }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(shirtService.update).toHaveBeenCalledWith(shirt);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Shirt>>();
      const shirt = new Shirt();
      jest.spyOn(shirtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shirt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shirt }));
      saveSubject.complete();

      // THEN
      expect(shirtService.create).toHaveBeenCalledWith(shirt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Shirt>>();
      const shirt = { id: 123 };
      jest.spyOn(shirtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shirt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shirtService.update).toHaveBeenCalledWith(shirt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTeamById', () => {
      it('Should return tracked Team primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTeamById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSeasonById', () => {
      it('Should return tracked Season primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSeasonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
