import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LocalAssociationRegionService } from '../service/local-association-region.service';
import { ILocalAssociationRegion, LocalAssociationRegion } from '../local-association-region.model';
import { IAssociation } from 'app/entities/association/association.model';
import { AssociationService } from 'app/entities/association/service/association.service';

import { LocalAssociationRegionUpdateComponent } from './local-association-region-update.component';

describe('LocalAssociationRegion Management Update Component', () => {
  let comp: LocalAssociationRegionUpdateComponent;
  let fixture: ComponentFixture<LocalAssociationRegionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localAssociationRegionService: LocalAssociationRegionService;
  let associationService: AssociationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LocalAssociationRegionUpdateComponent],
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
      .overrideTemplate(LocalAssociationRegionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocalAssociationRegionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localAssociationRegionService = TestBed.inject(LocalAssociationRegionService);
    associationService = TestBed.inject(AssociationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Association query and add missing value', () => {
      const localAssociationRegion: ILocalAssociationRegion = { id: 456 };
      const association: IAssociation = { id: 16023 };
      localAssociationRegion.association = association;

      const associationCollection: IAssociation[] = [{ id: 59271 }];
      jest.spyOn(associationService, 'query').mockReturnValue(of(new HttpResponse({ body: associationCollection })));
      const additionalAssociations = [association];
      const expectedCollection: IAssociation[] = [...additionalAssociations, ...associationCollection];
      jest.spyOn(associationService, 'addAssociationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ localAssociationRegion });
      comp.ngOnInit();

      expect(associationService.query).toHaveBeenCalled();
      expect(associationService.addAssociationToCollectionIfMissing).toHaveBeenCalledWith(associationCollection, ...additionalAssociations);
      expect(comp.associationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const localAssociationRegion: ILocalAssociationRegion = { id: 456 };
      const association: IAssociation = { id: 3623 };
      localAssociationRegion.association = association;

      activatedRoute.data = of({ localAssociationRegion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(localAssociationRegion));
      expect(comp.associationsSharedCollection).toContain(association);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LocalAssociationRegion>>();
      const localAssociationRegion = { id: 123 };
      jest.spyOn(localAssociationRegionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localAssociationRegion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localAssociationRegion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(localAssociationRegionService.update).toHaveBeenCalledWith(localAssociationRegion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LocalAssociationRegion>>();
      const localAssociationRegion = new LocalAssociationRegion();
      jest.spyOn(localAssociationRegionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localAssociationRegion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localAssociationRegion }));
      saveSubject.complete();

      // THEN
      expect(localAssociationRegionService.create).toHaveBeenCalledWith(localAssociationRegion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LocalAssociationRegion>>();
      const localAssociationRegion = { id: 123 };
      jest.spyOn(localAssociationRegionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localAssociationRegion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localAssociationRegionService.update).toHaveBeenCalledWith(localAssociationRegion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAssociationById', () => {
      it('Should return tracked Association primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssociationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
