import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SystemConfigurationService } from '../service/system-configuration.service';
import { ISystemConfiguration, SystemConfiguration } from '../system-configuration.model';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';
import { ISponsor } from 'app/entities/sponsor/sponsor.model';
import { SponsorService } from 'app/entities/sponsor/service/sponsor.service';

import { SystemConfigurationUpdateComponent } from './system-configuration-update.component';

describe('SystemConfiguration Management Update Component', () => {
  let comp: SystemConfigurationUpdateComponent;
  let fixture: ComponentFixture<SystemConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let systemConfigurationService: SystemConfigurationService;
  let seasonService: SeasonService;
  let sponsorService: SponsorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SystemConfigurationUpdateComponent],
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
      .overrideTemplate(SystemConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SystemConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemConfigurationService = TestBed.inject(SystemConfigurationService);
    seasonService = TestBed.inject(SeasonService);
    sponsorService = TestBed.inject(SponsorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call currentSeason query and add missing value', () => {
      const systemConfiguration: ISystemConfiguration = { id: 456 };
      const currentSeason: ISeason = { id: 34224 };
      systemConfiguration.currentSeason = currentSeason;

      const currentSeasonCollection: ISeason[] = [{ id: 76994 }];
      jest.spyOn(seasonService, 'query').mockReturnValue(of(new HttpResponse({ body: currentSeasonCollection })));
      const expectedCollection: ISeason[] = [currentSeason, ...currentSeasonCollection];
      jest.spyOn(seasonService, 'addSeasonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      expect(seasonService.query).toHaveBeenCalled();
      expect(seasonService.addSeasonToCollectionIfMissing).toHaveBeenCalledWith(currentSeasonCollection, currentSeason);
      expect(comp.currentSeasonsCollection).toEqual(expectedCollection);
    });

    it('Should call defaultSponsorLogo query and add missing value', () => {
      const systemConfiguration: ISystemConfiguration = { id: 456 };
      const defaultSponsorLogo: ISponsor = { id: 33503 };
      systemConfiguration.defaultSponsorLogo = defaultSponsorLogo;

      const defaultSponsorLogoCollection: ISponsor[] = [{ id: 62055 }];
      jest.spyOn(sponsorService, 'query').mockReturnValue(of(new HttpResponse({ body: defaultSponsorLogoCollection })));
      const expectedCollection: ISponsor[] = [defaultSponsorLogo, ...defaultSponsorLogoCollection];
      jest.spyOn(sponsorService, 'addSponsorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      expect(sponsorService.query).toHaveBeenCalled();
      expect(sponsorService.addSponsorToCollectionIfMissing).toHaveBeenCalledWith(defaultSponsorLogoCollection, defaultSponsorLogo);
      expect(comp.defaultSponsorLogosCollection).toEqual(expectedCollection);
    });

    it('Should call defaultMotmSponsorLogo query and add missing value', () => {
      const systemConfiguration: ISystemConfiguration = { id: 456 };
      const defaultMotmSponsorLogo: ISponsor = { id: 30016 };
      systemConfiguration.defaultMotmSponsorLogo = defaultMotmSponsorLogo;

      const defaultMotmSponsorLogoCollection: ISponsor[] = [{ id: 92507 }];
      jest.spyOn(sponsorService, 'query').mockReturnValue(of(new HttpResponse({ body: defaultMotmSponsorLogoCollection })));
      const expectedCollection: ISponsor[] = [defaultMotmSponsorLogo, ...defaultMotmSponsorLogoCollection];
      jest.spyOn(sponsorService, 'addSponsorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      expect(sponsorService.query).toHaveBeenCalled();
      expect(sponsorService.addSponsorToCollectionIfMissing).toHaveBeenCalledWith(defaultMotmSponsorLogoCollection, defaultMotmSponsorLogo);
      expect(comp.defaultMotmSponsorLogosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const systemConfiguration: ISystemConfiguration = { id: 456 };
      const currentSeason: ISeason = { id: 52398 };
      systemConfiguration.currentSeason = currentSeason;
      const defaultSponsorLogo: ISponsor = { id: 84862 };
      systemConfiguration.defaultSponsorLogo = defaultSponsorLogo;
      const defaultMotmSponsorLogo: ISponsor = { id: 9855 };
      systemConfiguration.defaultMotmSponsorLogo = defaultMotmSponsorLogo;

      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(systemConfiguration));
      expect(comp.currentSeasonsCollection).toContain(currentSeason);
      expect(comp.defaultSponsorLogosCollection).toContain(defaultSponsorLogo);
      expect(comp.defaultMotmSponsorLogosCollection).toContain(defaultMotmSponsorLogo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SystemConfiguration>>();
      const systemConfiguration = { id: 123 };
      jest.spyOn(systemConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemConfiguration }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemConfigurationService.update).toHaveBeenCalledWith(systemConfiguration);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SystemConfiguration>>();
      const systemConfiguration = new SystemConfiguration();
      jest.spyOn(systemConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemConfiguration }));
      saveSubject.complete();

      // THEN
      expect(systemConfigurationService.create).toHaveBeenCalledWith(systemConfiguration);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SystemConfiguration>>();
      const systemConfiguration = { id: 123 };
      jest.spyOn(systemConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemConfigurationService.update).toHaveBeenCalledWith(systemConfiguration);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSeasonById', () => {
      it('Should return tracked Season primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSeasonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSponsorById', () => {
      it('Should return tracked Sponsor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSponsorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
