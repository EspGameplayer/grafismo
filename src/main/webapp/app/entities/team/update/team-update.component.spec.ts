import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TeamService } from '../service/team.service';
import { ITeam, Team } from '../team.model';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { IStadium } from 'app/entities/stadium/stadium.model';
import { StadiumService } from 'app/entities/stadium/service/stadium.service';

import { TeamUpdateComponent } from './team-update.component';

describe('Team Management Update Component', () => {
  let comp: TeamUpdateComponent;
  let fixture: ComponentFixture<TeamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teamService: TeamService;
  let formationService: FormationService;
  let stadiumService: StadiumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TeamUpdateComponent],
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
      .overrideTemplate(TeamUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teamService = TestBed.inject(TeamService);
    formationService = TestBed.inject(FormationService);
    stadiumService = TestBed.inject(StadiumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Formation query and add missing value', () => {
      const team: ITeam = { id: 456 };
      const preferredFormation: IFormation = { id: 34345 };
      team.preferredFormation = preferredFormation;

      const formationCollection: IFormation[] = [{ id: 55803 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [preferredFormation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ team });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(formationCollection, ...additionalFormations);
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Stadium query and add missing value', () => {
      const team: ITeam = { id: 456 };
      const stadiums: IStadium[] = [{ id: 20655 }];
      team.stadiums = stadiums;

      const stadiumCollection: IStadium[] = [{ id: 29364 }];
      jest.spyOn(stadiumService, 'query').mockReturnValue(of(new HttpResponse({ body: stadiumCollection })));
      const additionalStadiums = [...stadiums];
      const expectedCollection: IStadium[] = [...additionalStadiums, ...stadiumCollection];
      jest.spyOn(stadiumService, 'addStadiumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ team });
      comp.ngOnInit();

      expect(stadiumService.query).toHaveBeenCalled();
      expect(stadiumService.addStadiumToCollectionIfMissing).toHaveBeenCalledWith(stadiumCollection, ...additionalStadiums);
      expect(comp.stadiumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const team: ITeam = { id: 456 };
      const preferredFormation: IFormation = { id: 30426 };
      team.preferredFormation = preferredFormation;
      const stadiums: IStadium = { id: 70679 };
      team.stadiums = [stadiums];

      activatedRoute.data = of({ team });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(team));
      expect(comp.formationsSharedCollection).toContain(preferredFormation);
      expect(comp.stadiumsSharedCollection).toContain(stadiums);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Team>>();
      const team = { id: 123 };
      jest.spyOn(teamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ team });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: team }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(teamService.update).toHaveBeenCalledWith(team);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Team>>();
      const team = new Team();
      jest.spyOn(teamService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ team });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: team }));
      saveSubject.complete();

      // THEN
      expect(teamService.create).toHaveBeenCalledWith(team);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Team>>();
      const team = { id: 123 };
      jest.spyOn(teamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ team });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teamService.update).toHaveBeenCalledWith(team);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFormationById', () => {
      it('Should return tracked Formation primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackStadiumById', () => {
      it('Should return tracked Stadium primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStadiumById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedStadium', () => {
      it('Should return option if no Stadium is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedStadium(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Stadium for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedStadium(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Stadium is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedStadium(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
