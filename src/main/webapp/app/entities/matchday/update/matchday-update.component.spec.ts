import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatchdayService } from '../service/matchday.service';
import { IMatchday, Matchday } from '../matchday.model';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

import { MatchdayUpdateComponent } from './matchday-update.component';

describe('Matchday Management Update Component', () => {
  let comp: MatchdayUpdateComponent;
  let fixture: ComponentFixture<MatchdayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matchdayService: MatchdayService;
  let competitionService: CompetitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatchdayUpdateComponent],
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
      .overrideTemplate(MatchdayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatchdayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matchdayService = TestBed.inject(MatchdayService);
    competitionService = TestBed.inject(CompetitionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Competition query and add missing value', () => {
      const matchday: IMatchday = { id: 456 };
      const competition: ICompetition = { id: 59859 };
      matchday.competition = competition;

      const competitionCollection: ICompetition[] = [{ id: 60962 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [competition];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matchday });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(competitionCollection, ...additionalCompetitions);
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const matchday: IMatchday = { id: 456 };
      const competition: ICompetition = { id: 74900 };
      matchday.competition = competition;

      activatedRoute.data = of({ matchday });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(matchday));
      expect(comp.competitionsSharedCollection).toContain(competition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matchday>>();
      const matchday = { id: 123 };
      jest.spyOn(matchdayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchday });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matchday }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(matchdayService.update).toHaveBeenCalledWith(matchday);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matchday>>();
      const matchday = new Matchday();
      jest.spyOn(matchdayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchday });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matchday }));
      saveSubject.complete();

      // THEN
      expect(matchdayService.create).toHaveBeenCalledWith(matchday);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matchday>>();
      const matchday = { id: 123 };
      jest.spyOn(matchdayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchday });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matchdayService.update).toHaveBeenCalledWith(matchday);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCompetitionById', () => {
      it('Should return tracked Competition primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetitionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
