import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeductionService } from '../service/deduction.service';
import { IDeduction, Deduction } from '../deduction.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

import { DeductionUpdateComponent } from './deduction-update.component';

describe('Deduction Management Update Component', () => {
  let comp: DeductionUpdateComponent;
  let fixture: ComponentFixture<DeductionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deductionService: DeductionService;
  let teamService: TeamService;
  let competitionService: CompetitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeductionUpdateComponent],
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
      .overrideTemplate(DeductionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeductionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deductionService = TestBed.inject(DeductionService);
    teamService = TestBed.inject(TeamService);
    competitionService = TestBed.inject(CompetitionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Team query and add missing value', () => {
      const deduction: IDeduction = { id: 456 };
      const team: ITeam = { id: 26956 };
      deduction.team = team;

      const teamCollection: ITeam[] = [{ id: 69465 }];
      jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
      const additionalTeams = [team];
      const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
      jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      expect(teamService.query).toHaveBeenCalled();
      expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
      expect(comp.teamsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Competition query and add missing value', () => {
      const deduction: IDeduction = { id: 456 };
      const competition: ICompetition = { id: 42828 };
      deduction.competition = competition;

      const competitionCollection: ICompetition[] = [{ id: 82331 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [competition];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(competitionCollection, ...additionalCompetitions);
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deduction: IDeduction = { id: 456 };
      const team: ITeam = { id: 71855 };
      deduction.team = team;
      const competition: ICompetition = { id: 23832 };
      deduction.competition = competition;

      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deduction));
      expect(comp.teamsSharedCollection).toContain(team);
      expect(comp.competitionsSharedCollection).toContain(competition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deduction>>();
      const deduction = { id: 123 };
      jest.spyOn(deductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deduction }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deductionService.update).toHaveBeenCalledWith(deduction);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deduction>>();
      const deduction = new Deduction();
      jest.spyOn(deductionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deduction }));
      saveSubject.complete();

      // THEN
      expect(deductionService.create).toHaveBeenCalledWith(deduction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deduction>>();
      const deduction = { id: 123 };
      jest.spyOn(deductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deductionService.update).toHaveBeenCalledWith(deduction);
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

    describe('trackCompetitionById', () => {
      it('Should return tracked Competition primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetitionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
