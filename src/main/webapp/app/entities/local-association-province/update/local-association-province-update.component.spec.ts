import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LocalAssociationProvinceService } from '../service/local-association-province.service';
import { ILocalAssociationProvince, LocalAssociationProvince } from '../local-association-province.model';
import { ILocalAssociationRegion } from 'app/entities/local-association-region/local-association-region.model';
import { LocalAssociationRegionService } from 'app/entities/local-association-region/service/local-association-region.service';

import { LocalAssociationProvinceUpdateComponent } from './local-association-province-update.component';

describe('LocalAssociationProvince Management Update Component', () => {
  let comp: LocalAssociationProvinceUpdateComponent;
  let fixture: ComponentFixture<LocalAssociationProvinceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localAssociationProvinceService: LocalAssociationProvinceService;
  let localAssociationRegionService: LocalAssociationRegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LocalAssociationProvinceUpdateComponent],
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
      .overrideTemplate(LocalAssociationProvinceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocalAssociationProvinceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localAssociationProvinceService = TestBed.inject(LocalAssociationProvinceService);
    localAssociationRegionService = TestBed.inject(LocalAssociationRegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call LocalAssociationRegion query and add missing value', () => {
      const localAssociationProvince: ILocalAssociationProvince = { id: 456 };
      const association: ILocalAssociationRegion = { id: 25386 };
      localAssociationProvince.association = association;

      const localAssociationRegionCollection: ILocalAssociationRegion[] = [{ id: 35891 }];
      jest.spyOn(localAssociationRegionService, 'query').mockReturnValue(of(new HttpResponse({ body: localAssociationRegionCollection })));
      const additionalLocalAssociationRegions = [association];
      const expectedCollection: ILocalAssociationRegion[] = [...additionalLocalAssociationRegions, ...localAssociationRegionCollection];
      jest.spyOn(localAssociationRegionService, 'addLocalAssociationRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ localAssociationProvince });
      comp.ngOnInit();

      expect(localAssociationRegionService.query).toHaveBeenCalled();
      expect(localAssociationRegionService.addLocalAssociationRegionToCollectionIfMissing).toHaveBeenCalledWith(
        localAssociationRegionCollection,
        ...additionalLocalAssociationRegions
      );
      expect(comp.localAssociationRegionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const localAssociationProvince: ILocalAssociationProvince = { id: 456 };
      const association: ILocalAssociationRegion = { id: 56923 };
      localAssociationProvince.association = association;

      activatedRoute.data = of({ localAssociationProvince });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(localAssociationProvince));
      expect(comp.localAssociationRegionsSharedCollection).toContain(association);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LocalAssociationProvince>>();
      const localAssociationProvince = { id: 123 };
      jest.spyOn(localAssociationProvinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localAssociationProvince });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localAssociationProvince }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(localAssociationProvinceService.update).toHaveBeenCalledWith(localAssociationProvince);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LocalAssociationProvince>>();
      const localAssociationProvince = new LocalAssociationProvince();
      jest.spyOn(localAssociationProvinceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localAssociationProvince });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localAssociationProvince }));
      saveSubject.complete();

      // THEN
      expect(localAssociationProvinceService.create).toHaveBeenCalledWith(localAssociationProvince);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LocalAssociationProvince>>();
      const localAssociationProvince = { id: 123 };
      jest.spyOn(localAssociationProvinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localAssociationProvince });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localAssociationProvinceService.update).toHaveBeenCalledWith(localAssociationProvince);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLocalAssociationRegionById', () => {
      it('Should return tracked LocalAssociationRegion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocalAssociationRegionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
