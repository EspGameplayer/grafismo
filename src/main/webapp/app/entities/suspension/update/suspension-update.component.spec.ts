import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SuspensionService } from '../service/suspension.service';
import { ISuspension, Suspension } from '../suspension.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

import { SuspensionUpdateComponent } from './suspension-update.component';

describe('Suspension Management Update Component', () => {
  let comp: SuspensionUpdateComponent;
  let fixture: ComponentFixture<SuspensionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let suspensionService: SuspensionService;
  let personService: PersonService;
  let competitionService: CompetitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SuspensionUpdateComponent],
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
      .overrideTemplate(SuspensionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SuspensionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    suspensionService = TestBed.inject(SuspensionService);
    personService = TestBed.inject(PersonService);
    competitionService = TestBed.inject(CompetitionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Person query and add missing value', () => {
      const suspension: ISuspension = { id: 456 };
      const person: IPerson = { id: 14053 };
      suspension.person = person;

      const personCollection: IPerson[] = [{ id: 52064 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const additionalPeople = [person];
      const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ suspension });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Competition query and add missing value', () => {
      const suspension: ISuspension = { id: 456 };
      const competition: ICompetition = { id: 27197 };
      suspension.competition = competition;

      const competitionCollection: ICompetition[] = [{ id: 34804 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [competition];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ suspension });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(competitionCollection, ...additionalCompetitions);
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const suspension: ISuspension = { id: 456 };
      const person: IPerson = { id: 52358 };
      suspension.person = person;
      const competition: ICompetition = { id: 69253 };
      suspension.competition = competition;

      activatedRoute.data = of({ suspension });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(suspension));
      expect(comp.peopleSharedCollection).toContain(person);
      expect(comp.competitionsSharedCollection).toContain(competition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Suspension>>();
      const suspension = { id: 123 };
      jest.spyOn(suspensionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ suspension });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: suspension }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(suspensionService.update).toHaveBeenCalledWith(suspension);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Suspension>>();
      const suspension = new Suspension();
      jest.spyOn(suspensionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ suspension });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: suspension }));
      saveSubject.complete();

      // THEN
      expect(suspensionService.create).toHaveBeenCalledWith(suspension);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Suspension>>();
      const suspension = { id: 123 };
      jest.spyOn(suspensionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ suspension });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(suspensionService.update).toHaveBeenCalledWith(suspension);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonById', () => {
      it('Should return tracked Person primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonById(0, entity);
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
