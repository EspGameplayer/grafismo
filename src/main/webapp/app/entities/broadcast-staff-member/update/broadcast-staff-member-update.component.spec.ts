import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BroadcastStaffMemberService } from '../service/broadcast-staff-member.service';
import { IBroadcastStaffMember, BroadcastStaffMember } from '../broadcast-staff-member.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { BroadcastStaffMemberUpdateComponent } from './broadcast-staff-member-update.component';

describe('BroadcastStaffMember Management Update Component', () => {
  let comp: BroadcastStaffMemberUpdateComponent;
  let fixture: ComponentFixture<BroadcastStaffMemberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let broadcastStaffMemberService: BroadcastStaffMemberService;
  let personService: PersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BroadcastStaffMemberUpdateComponent],
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
      .overrideTemplate(BroadcastStaffMemberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BroadcastStaffMemberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    broadcastStaffMemberService = TestBed.inject(BroadcastStaffMemberService);
    personService = TestBed.inject(PersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call person query and add missing value', () => {
      const broadcastStaffMember: IBroadcastStaffMember = { id: 456 };
      const person: IPerson = { id: 43250 };
      broadcastStaffMember.person = person;

      const personCollection: IPerson[] = [{ id: 64999 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IPerson[] = [person, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ broadcastStaffMember });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const broadcastStaffMember: IBroadcastStaffMember = { id: 456 };
      const person: IPerson = { id: 12162 };
      broadcastStaffMember.person = person;

      activatedRoute.data = of({ broadcastStaffMember });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(broadcastStaffMember));
      expect(comp.peopleCollection).toContain(person);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BroadcastStaffMember>>();
      const broadcastStaffMember = { id: 123 };
      jest.spyOn(broadcastStaffMemberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ broadcastStaffMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: broadcastStaffMember }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(broadcastStaffMemberService.update).toHaveBeenCalledWith(broadcastStaffMember);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BroadcastStaffMember>>();
      const broadcastStaffMember = new BroadcastStaffMember();
      jest.spyOn(broadcastStaffMemberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ broadcastStaffMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: broadcastStaffMember }));
      saveSubject.complete();

      // THEN
      expect(broadcastStaffMemberService.create).toHaveBeenCalledWith(broadcastStaffMember);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BroadcastStaffMember>>();
      const broadcastStaffMember = { id: 123 };
      jest.spyOn(broadcastStaffMemberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ broadcastStaffMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(broadcastStaffMemberService.update).toHaveBeenCalledWith(broadcastStaffMember);
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
  });
});
