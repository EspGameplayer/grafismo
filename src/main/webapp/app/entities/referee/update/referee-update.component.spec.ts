import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RefereeService } from '../service/referee.service';
import { IReferee, Referee } from '../referee.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ILocalAssociationProvince } from 'app/entities/local-association-province/local-association-province.model';
import { LocalAssociationProvinceService } from 'app/entities/local-association-province/service/local-association-province.service';

import { RefereeUpdateComponent } from './referee-update.component';

describe('Referee Management Update Component', () => {
  let comp: RefereeUpdateComponent;
  let fixture: ComponentFixture<RefereeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let refereeService: RefereeService;
  let personService: PersonService;
  let localAssociationProvinceService: LocalAssociationProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RefereeUpdateComponent],
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
      .overrideTemplate(RefereeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RefereeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    refereeService = TestBed.inject(RefereeService);
    personService = TestBed.inject(PersonService);
    localAssociationProvinceService = TestBed.inject(LocalAssociationProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call person query and add missing value', () => {
      const referee: IReferee = { id: 456 };
      const person: IPerson = { id: 95546 };
      referee.person = person;

      const personCollection: IPerson[] = [{ id: 16223 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IPerson[] = [person, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ referee });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call LocalAssociationProvince query and add missing value', () => {
      const referee: IReferee = { id: 456 };
      const association: ILocalAssociationProvince = { id: 88200 };
      referee.association = association;

      const localAssociationProvinceCollection: ILocalAssociationProvince[] = [{ id: 46792 }];
      jest
        .spyOn(localAssociationProvinceService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: localAssociationProvinceCollection })));
      const additionalLocalAssociationProvinces = [association];
      const expectedCollection: ILocalAssociationProvince[] = [
        ...additionalLocalAssociationProvinces,
        ...localAssociationProvinceCollection,
      ];
      jest.spyOn(localAssociationProvinceService, 'addLocalAssociationProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ referee });
      comp.ngOnInit();

      expect(localAssociationProvinceService.query).toHaveBeenCalled();
      expect(localAssociationProvinceService.addLocalAssociationProvinceToCollectionIfMissing).toHaveBeenCalledWith(
        localAssociationProvinceCollection,
        ...additionalLocalAssociationProvinces
      );
      expect(comp.localAssociationProvincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const referee: IReferee = { id: 456 };
      const person: IPerson = { id: 34465 };
      referee.person = person;
      const association: ILocalAssociationProvince = { id: 48275 };
      referee.association = association;

      activatedRoute.data = of({ referee });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(referee));
      expect(comp.peopleCollection).toContain(person);
      expect(comp.localAssociationProvincesSharedCollection).toContain(association);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Referee>>();
      const referee = { id: 123 };
      jest.spyOn(refereeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ referee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: referee }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(refereeService.update).toHaveBeenCalledWith(referee);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Referee>>();
      const referee = new Referee();
      jest.spyOn(refereeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ referee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: referee }));
      saveSubject.complete();

      // THEN
      expect(refereeService.create).toHaveBeenCalledWith(referee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Referee>>();
      const referee = { id: 123 };
      jest.spyOn(refereeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ referee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(refereeService.update).toHaveBeenCalledWith(referee);
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

    describe('trackLocalAssociationProvinceById', () => {
      it('Should return tracked LocalAssociationProvince primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocalAssociationProvinceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
