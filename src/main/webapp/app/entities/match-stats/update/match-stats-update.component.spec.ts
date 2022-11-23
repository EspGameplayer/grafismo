import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatchStatsService } from '../service/match-stats.service';
import { IMatchStats, MatchStats } from '../match-stats.model';
import { IMatch } from 'app/entities/match/match.model';
import { MatchService } from 'app/entities/match/service/match.service';

import { MatchStatsUpdateComponent } from './match-stats-update.component';

describe('MatchStats Management Update Component', () => {
  let comp: MatchStatsUpdateComponent;
  let fixture: ComponentFixture<MatchStatsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matchStatsService: MatchStatsService;
  let matchService: MatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatchStatsUpdateComponent],
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
      .overrideTemplate(MatchStatsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatchStatsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matchStatsService = TestBed.inject(MatchStatsService);
    matchService = TestBed.inject(MatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call match query and add missing value', () => {
      const matchStats: IMatchStats = { id: 456 };
      const match: IMatch = { id: 81386 };
      matchStats.match = match;

      const matchCollection: IMatch[] = [{ id: 24598 }];
      jest.spyOn(matchService, 'query').mockReturnValue(of(new HttpResponse({ body: matchCollection })));
      const expectedCollection: IMatch[] = [match, ...matchCollection];
      jest.spyOn(matchService, 'addMatchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matchStats });
      comp.ngOnInit();

      expect(matchService.query).toHaveBeenCalled();
      expect(matchService.addMatchToCollectionIfMissing).toHaveBeenCalledWith(matchCollection, match);
      expect(comp.matchesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const matchStats: IMatchStats = { id: 456 };
      const match: IMatch = { id: 10560 };
      matchStats.match = match;

      activatedRoute.data = of({ matchStats });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(matchStats));
      expect(comp.matchesCollection).toContain(match);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MatchStats>>();
      const matchStats = { id: 123 };
      jest.spyOn(matchStatsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchStats });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matchStats }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(matchStatsService.update).toHaveBeenCalledWith(matchStats);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MatchStats>>();
      const matchStats = new MatchStats();
      jest.spyOn(matchStatsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchStats });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matchStats }));
      saveSubject.complete();

      // THEN
      expect(matchStatsService.create).toHaveBeenCalledWith(matchStats);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MatchStats>>();
      const matchStats = { id: 123 };
      jest.spyOn(matchStatsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchStats });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matchStatsService.update).toHaveBeenCalledWith(matchStats);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMatchById', () => {
      it('Should return tracked Match primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatchById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
